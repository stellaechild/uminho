from flask import Flask, render_template, request, session, redirect, url_for
from flask_cors import CORS
import random
from gen_questions import generate_questions
import os
from threading import Lock

app = Flask(__name__)
app.secret_key = 'your_secret_key_here'
CORS(app)  # enable cross-origin resource sharing

def shuffle_list(value):
    temp_list = list(value)
    random.shuffle(temp_list)
    return temp_list

app.jinja_env.filters['shuffle'] = shuffle_list

questions_storage = {}  # store questions for each session
questions_lock = Lock()  # lock for thread-safe access to questions_storage

@app.before_request
def cleanup_questions_storage():
    max_sessions = 100
    with questions_lock:
        if len(questions_storage) > max_sessions:
            oldest_sessions = list(questions_storage.keys())[:len(questions_storage) - max_sessions]
            for session_id in oldest_sessions:
                del questions_storage[session_id]


@app.route('/', methods=['GET', 'POST'])
def home():
    if request.method == 'POST':
        # get quiz settings from form
        modes = request.form.getlist('mode')
        theme = request.form.get('theme')
        question_count = int(request.form.get('question_count', 20))

        print(f"\nTotal Questions: {question_count}")
        print(f"Modes: {modes}")

        # store settings in session
        session['modes'] = modes
        session['theme'] = theme
        session['question_count'] = question_count
        session['score'] = 0
        session['questions_answered'] = 0
        session.pop('current_question', None)

        # generate a unique session id
        session_id = str(random.randint(100000, 999999))
        session['session_id'] = session_id

        # generate and store questions for this session
        all_questions = generate_questions(theme, question_count, modes)
        with questions_lock:
            questions_storage[session_id] = (
                all_questions["true_false"] +
                all_questions["multiple_choice"] +
                all_questions["matching"]
            )
        random.shuffle(questions_storage[session_id])

        # print debug info about question counts
        print(f"Quiz TF Count: {len(all_questions['true_false'])}")
        print(f"Quiz MC Count: {len(all_questions['multiple_choice'])}")
        print(f"Quiz Match Count: {len(all_questions['matching'])}\n")

        return redirect(url_for('quiz'))
    
    return render_template('home.html')

@app.route('/quiz', methods=['GET', 'POST'])
def quiz():
    session_id = session.get('session_id')
    if not session_id:
        return redirect(url_for('home'))

    with questions_lock:
        if session_id not in questions_storage:
            return redirect(url_for('home'))

    if request.method == 'POST':
        # process user's answer
        user_answer = request.form.get('answer')
        question_text = request.form.get('question')
        current_question = session.get('current_question')
        
        if current_question and current_question['question'] == question_text:
            # check if the answer is correct
            correct_pairs = []
            correct_answer = None
            correct_info = current_question.get('correct_info')
            correct_info2 = current_question.get('correct_info2')
            if "pairs" in current_question:
                user_pairs = request.form.getlist('pairs')
                if user_pairs:
                    try:
                        user_pairs = [tuple(pair.split('|')) for pair in user_pairs if '|' in pair]
                    except Exception:
                        user_pairs = []
                else:
                    user_pairs = []
                correct_pairs = current_question['pairs']
                correct_answer = correct_pairs
                correct = sorted(user_pairs) == sorted(correct_pairs)
                attempted_answer = user_pairs
            else:
                correct = current_question['answer'] == user_answer
                correct_answer = current_question['answer']
                attempted_answer = user_answer
            
            # update session with score and answered questions
            session['score'] = session.get('score', 0) + (1 if correct else 0)
            session['questions_answered'] = session.get('questions_answered', 0) + 1
            
            # store result data for the result page
            session['result_data'] = {
                'question': current_question['question'],
                'correct': correct,
                'correct_answer': correct_answer,
                'attempted_answer': attempted_answer,
                'correct_info': correct_info,
                'correct_info2': correct_info2,
                'final': session['questions_answered'] >= session['question_count'],
                'theme': session.get('theme')
            }
            
            session.pop('current_question', None)  # clear current question
            
            return redirect(url_for('result'))
    
    # fetch the next question
    current_question = session.get('current_question')
    if not current_question:
        with questions_lock:
            question = questions_storage[session_id].pop(0)
        session['current_question'] = question
    else:
        question = current_question

    question_number = session.get('questions_answered', 0) + 1
    theme = session.get('theme')
    if "pairs" in question:
        return render_template(
            'matchQs.html',
            question=question,
            question_number=question_number,
            total_questions=session['question_count'],
            theme=theme
        )
    else:
        return render_template(
            'choiceQs.html',
            question=question,
            question_number=question_number,
            total_questions=session['question_count'],
            theme=theme
        )

@app.route('/result')
def result():
    result_data = session.get('result_data')
    if not result_data:
        return redirect(url_for('quiz'))
    
    return render_template(
        'result.html',
        question=result_data['question'],
        correct=result_data['correct'],
        correct_answer=result_data['correct_answer'],
        attempted_answer=result_data['attempted_answer'],
        correct_info=result_data.get('correct_info'),
        correct_info2=result_data.get('correct_info2'),
        score=session.get('score', 0),
        final=result_data['final'],
        total_questions=session['question_count'],
        theme=result_data.get('theme')
    )

@app.route('/end')
def end():
    session_id = session.pop('session_id', None)
    if session_id:
        with questions_lock:
            if session_id in questions_storage:
                del questions_storage[session_id]  # clear session-specific questions
    session['questions_answered'] = 0
    return render_template('end.html', score=session.get('score', 0), total_questions=session['question_count'])

app.template_folder = 'web_templates'

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 3000))
    app.run(host='0.0.0.0', port=port, debug=False)

