# Um jogo sobre a história de Portugal
# 2025-02-24 jcr
#
from flask import Flask, render_template, jsonify, request, session, redirect, url_for
from flask_cors import CORS
import random
import requests

app = Flask(__name__)
app.secret_key = 'História de Portugal'
CORS(app)

# Retrieve info from GraphDB
def query_graphdb(endpoint_url, sparql_query):
    headers = {
        'Accept': 'application/json', 
    }
    response = requests.get(endpoint_url, params={'query': sparql_query}, headers=headers)
    if response.status_code == 200:
        return response.json() 
    else:
        raise Exception(f"Error {response.status_code}: {response.text}")

endpoint = "http://localhost:7200/repositories/HistoriaPT"
sparql_query = """
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
SELECT ?n ?o
    WHERE {
        ?s a :Rei.
    	?s :nome ?n .
    	?s :nascimento ?o.
    }
"""
result = query_graphdb(endpoint, sparql_query)
listaReis = []
for r in result['results']['bindings']:
    t = {
            'nome': r['n']['value'].split('#')[-1], 
            'dataNasc': r['o']['value'].split('#')[-1]
    }
    listaReis.append(t)


@app.route('/')
def home():
    session['score'] = 0
    return redirect(url_for('quiz'))

@app.route('/quiz', methods=['GET'])
def generate_question():
    reis = random.choices(listaReis, k=4)
    reiSel = reis[random.randrange(0,4)]
    question = {
        "question": f"Quando nasceu o rei {reiSel['nome']}?",
        "options": [reis[0]['dataNasc'], reis[1]['dataNasc'], reis[2]['dataNasc'], reis[3]['dataNasc']],
        "answer": reiSel['dataNasc']
    }
    return render_template('quiz.html', question=question)

@app.route('/quiz', methods=['POST'])
def quiz():
    user_answer = request.form.get('answer')
    answer_correct = request.form.get('answerCorrect')
    correct = answer_correct == user_answer
    session['score'] = session.get('score', 0) + (1 if correct else 0)
    return render_template('result.html', correct=correct, correct_answer=answer_correct, score=session['score'])

@app.route('/score')
def score():
    return render_template('score.html', score=session.get('score', 0))

if __name__ == '__main__':
    app.run(debug=True)
