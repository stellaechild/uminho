# Backend: app.py (Flask server)
from flask import Flask, jsonify, request, session
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

@app.route('/api/quiz', methods=['GET'])
def get_question():
    question = random.choice(test_questions)
    return jsonify({
        'question': question['question'],
        'options': question['options']
    })

@app.route('/api/answer', methods=['POST'])
def check_answer():
    data = request.json
    user_answer = data.get('answer')
    question_text = data.get('question')
    
    for question in test_questions:
        if question['question'] == question_text:
            correct = question['answer'] == user_answer
            session['score'] = session.get('score', 0) + (1 if correct else 0)
            return jsonify({
                'correct': correct,
                'correct_answer': question['answer'],
                'score': session['score']
            })
    
    return jsonify({'error': 'Question not found'}), 404

if __name__ == '__main__':
    app.run(debug=True)
