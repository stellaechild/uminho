# Backend & Frontend: app.py (Flask with Jinja templates)
from flask import Flask, render_template, request, session, redirect, url_for
from flask_cors import CORS
from SPARQLWrapper import SPARQLWrapper, JSON
import random
import ssl
import certifi

# Override SSL context globally
ssl._create_default_https_context = ssl._create_unverified_context

app = Flask(__name__)
app.secret_key = 'your_secret_key_here'
CORS(app)

# Function to fetch questions from DBpedia
def fetch_questions_from_dbpedia():
    sparql = SPARQLWrapper("https://dbpedia.org/sparql")
    sparql.setQuery("""
        SELECT ?person ?personLabel ?occupationLabel WHERE {
          ?person a dbo:Person .
          ?person dbo:occupation ?occupation .
          ?person rdfs:label ?personLabel .
          ?occupation rdfs:label ?occupationLabel .
          FILTER (lang(?personLabel) = 'en' && lang(?occupationLabel) = 'en')
        } LIMIT 50
    """)
    sparql.setReturnFormat(JSON)
    results = sparql.query().convert()
    
    questions = []
    for result in results["results"]["bindings"]:
        person = result["personLabel"]["value"]
        occupation = result["occupationLabel"]["value"]
        question_text = f"What is the occupation of {person}?"
        options = [occupation]
        while len(options) < 4:
            random_occupation = random.choice(results["results"]["bindings"])["occupationLabel"]["value"]
            if random_occupation not in options:
                options.append(random_occupation)
        random.shuffle(options)
        
        questions.append({
            "question": question_text,
            "options": options,
            "answer": occupation
        })
        print(questions)
    return questions

@app.route('/')
def home():
    session['score'] = 0
    session['questions'] = fetch_questions_from_dbpedia()
    return redirect(url_for('quiz'))

@app.route('/quiz', methods=['GET', 'POST'])
def quiz():
    if request.method == 'POST':
        user_answer = request.form.get('answer')
        question_text = request.form.get('question')
        
        for question in session.get('questions', []):
            if question['question'] == question_text:
                correct = question['answer'] == user_answer
                session['score'] = session.get('score', 0) + (1 if correct else 0)
                return render_template('result.html', correct=correct, correct_answer=question['answer'], score=session['score'])
    
    if session.get('questions'):
        question = session['questions'].pop()
        return render_template('quiz.html', question=question)
    else:
        return redirect(url_for('score'))

@app.route('/score')
def score():
    return render_template('score.html', score=session.get('score', 0))

if __name__ == '__main__':
    app.run(debug=True)
