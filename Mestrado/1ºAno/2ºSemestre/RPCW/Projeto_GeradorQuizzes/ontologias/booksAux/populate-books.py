import pandas as pd
import re
import ast

def uri_safe(s):
    """Convert string to URI-safe format"""
    return re.sub(r'[^a-zA-Z0-9_]', '_', str(s))

def safe_literal(s):
    """Escape special characters for TTL literals"""
    return str(s).replace('"', '\\"')

def generate_ontology(csv_path, template_path, output_path):
    # Read CSV data
    df = pd.read_csv(csv_path)
    
    # Initialize TTL content with header
    ttl_content = []
    ttl_content.append("""@prefix : <http://www.semanticweb.org/books/> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .

<http://www.semanticweb.org/books> a owl:Ontology .
""")

    # Read template if exists
    try:
        with open(template_path, 'r', encoding='utf-8') as f:
            ttl_content.append(f.read())
    except FileNotFoundError:
        pass

    ttl_content.append("\n\n###  Generated Individuals  ###\n")

    # Process each book
    for _, row in df.iterrows():
        if not pd.notna(row.get('title')):
            continue
            
        book_id = uri_safe(row['title'])
        
        # Book individual with all properties
        book_entry = f"""
###  http://www.semanticweb.org/books/{book_id}
:{book_id} a owl:NamedIndividual , :Book ;
    :title "{safe_literal(row['title'])}" ;"""

        # Add data properties
        if pd.notna(row.get('pages')):
            book_entry += f"""
    :numberOfPages {int(row['pages'])} ;"""

        if pd.notna(row.get('firstPublishDate')):
            book_entry += f"""
    :publishedIn "{safe_literal(row['firstPublishDate'])}" ;"""

        # Add object properties
        if pd.notna(row.get('author')):
            authors = [a.strip() for a in str(row['author']).split(',') if a.strip()]
            for author in authors:
                author_id = uri_safe(author)
                book_entry += f"""
    :writtenBy :{author_id} ;"""

        if pd.notna(row.get('series')):
            series_id = uri_safe(row['series'])
            book_entry += f"""
    :partOfSeries :{series_id} ;"""

        if pd.notna(row.get('publisher')):
            pub_id = uri_safe(row['publisher'])
            book_entry += f"""
    :publishedBy :{pub_id} ;"""

        # Handle list-type columns
        for col in ['genres', 'characters', 'awards']:
            if col in row and pd.notna(row[col]):
                try:
                    items = ast.literal_eval(row[col]) if isinstance(row[col], str) else row[col]
                    for item in items:
                        item_id = uri_safe(item)
                        prop_name = {
                            'genres': 'hasGenre',
                            'characters': 'hasCharacter',
                            'awards': 'hasAwards'
                        }[col]
                        book_entry += f"""
    :{prop_name} :{item_id} ;"""
                except:
                    continue

        # Remove trailing semicolon and complete entry
        book_entry = book_entry.rstrip(';') + " .\n"
        ttl_content.append(book_entry)

        # Create related individuals with inverse properties
        if pd.notna(row.get('author')):
            for author in authors:
                author_id = uri_safe(author)
                ttl_content.append(f"""
:{author_id} a owl:NamedIndividual , :Author ;
    :name "{safe_literal(author)}" ;
    :hasWritten :{book_id} .
""")

        if pd.notna(row.get('series')):
            series_id = uri_safe(row['series'])
            ttl_content.append(f"""
:{series_id} a owl:NamedIndividual , :Series ;
    :title "{safe_literal(row['series'])}" ;
    :containsBook :{book_id} .
""")

        if pd.notna(row.get('publisher')):
            pub_id = uri_safe(row['publisher'])
            ttl_content.append(f"""
:{pub_id} a owl:NamedIndividual , :Publisher ;
    :name "{safe_literal(row['publisher'])}" ;
    :hasPublished :{book_id} .
""")

        # Create genre/character/award individuals with inverse properties
        for col in ['genres', 'characters', 'awards']:
            if col in row and pd.notna(row[col]):
                try:
                    items = ast.literal_eval(row[col]) if isinstance(row[col], str) else row[col]
                    for item in items:
                        item_id = uri_safe(item)
                        class_name = {
                            'genres': 'Genre',
                            'characters': 'Character',
                            'awards': 'Award'
                        }[col]
                        inverse_prop = {
                            'genres': 'hasBook',
                            'characters': 'appearsIn',
                            'awards': 'awardedBy'
                        }[col]
                        
                        ttl_content.append(f"""
:{item_id} a owl:NamedIndividual , :{class_name} ;
    :name "{safe_literal(item)}" ;
    :{inverse_prop} :{book_id} .
""")
                except:
                    continue

    # Write final file
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(ttl_content))

    print(f"Successfully generated ontology at {output_path}")

# Example usage
generate_ontology(
    csv_path="/home/mirtilo/Quiz_Generator/ontologias/books/cleaned-books.csv",
    template_path="/home/mirtilo/Quiz_Generator/ontologias/books/to-be-completed.ttl",
    output_path="/home/mirtilo/Quiz_Generator/ontologias/books.ttl"
)