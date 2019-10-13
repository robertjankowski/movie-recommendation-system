import pandas as pd
from flask import Flask, render_template, request
from recommendation.recommender import item_based_recom, rename_columns

app = Flask(__name__)
app.recommender = pd.read_parquet('models/recommender_df.parquet.gzip')
rename_columns(app.recommender)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/movie')
def recommend_movie_form():
    return render_template('movie_title_form.html', app=app)


@app.route('/movie', methods=['POST'])
def recommend_movie():
    movie_title = request.form['movie_title']
    try:
        df = item_based_recom(app.recommender, movie_title).head(10)
    except Exception:
        return render_template('error_title.html')
    # return render_template('movie.html', tables=[df.to_html(classes='data')], titles=df.columns.values)
    return render_template('movie.html', name='Movie recommendation for {}'.format(movie_title), data=df)


if __name__ == '__main__':
    app.run()
