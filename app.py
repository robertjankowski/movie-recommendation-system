from flask import Flask, render_template, request
import pandas as pd
from recommendation.recommender import item_based_recom

app = Flask(__name__)
app.recommender = pd.read_parquet('models/recommender_df.parquet.gzip')


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/movie')
def recommend_movie_form():
    return render_template('movie_title_form.html')


@app.route('/movie', methods=['POST'])
def recommend_movie():
    movie_title = request.form['movie_title']
    print(movie_title)
    example = 'Seven (a.k.a. Se7en) (1995)'
    try:
        df = item_based_recom(app.recommender, movie_title).head(10)
    except Exception:
        return render_template('error_title.html')
    return render_template('movie.html', tables=[df.to_html(classes='data')], titles=df.columns.values)


if __name__ == '__main__':
    app.run()
