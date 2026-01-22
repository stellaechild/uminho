# Backend & Frontend: app.py (Flask with Jinja templates)
from flask import Flask, render_template, jsonify, request, session, redirect, url_for
from flask_cors import CORS
import random

app = Flask(__name__)
app.secret_key = 'your_secret_key_here'
CORS(app)

# Mock questions for now
test_questions = [
    {
        "question": "Who painted the Mona Lisa?",
        "options": ["Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Claude Monet"],
        "answer": "Leonardo da Vinci"
    },
    {
        "question": "Albert Einstein was born in Germany.",
        "options": ["True", "False"],
        "answer": "True"
    },
    {
        "question": "In which year did World War II end?",
        "options": ["1942", "1945", "1939", "1950"],
        "answer": "1945"
    }
]

@app.route('/')
def home():
    session['score'] = 0
    return redirect(url_for('quiz'))

@app.route('/quiz', methods=['GET', 'POST'])
def quiz():
    if request.method == 'POST':
        user_answer = request.form.get('answer')
        question_text = request.form.get('question')
        
        for question in test_questions:
            if question['question'] == question_text:
                correct = question['answer'] == user_answer
                session['score'] = session.get('score', 0) + (1 if correct else 0)
                return render_template('result.html', correct=correct, correct_answer=question['answer'], score=session['score'])
    
    question = random.choice(test_questions)
    return render_template('quiz.html', question=question)

@app.route('/score')
def score():
    return render_template('score.html', score=session.get('score', 0))

if __name__ == '__main__':
    app.run(debug=True)
