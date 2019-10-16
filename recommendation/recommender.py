import pandas as pd
import re
import sklearn.metrics.pairwise as pw

categories = ['Film-Noir', 'Adventure', 'Children', 'IMAX', 'Crime', 'Documentary', 'Fantasy', 'Musical', 'Romance',
              'Mystery', 'Thriller', 'Animation', 'Action', 'Comedy', 'War', 'Drama', 'Western', 'Sci-Fi', 'Horror']


def item_based_recom(recommender_df, film_name):
    film_name = remove_date_tolower_title(film_name)
    cosine_df = pd.DataFrame(recommender_df[film_name].sort_values(ascending=False))
    cosine_df.reset_index(level=0, inplace=True)
    cosine_df.columns = ['title', 'cosine_sim']
    return cosine_df


def item_and_genre_based_recom(recommender_df, movies, categories):
    top_cos_genre = pd.merge(recommender_df, movies, on='title')
    top_cos_genre['genre_similarity'] = [pairwise_row_diff(top_cos_genre, 0, row, categories) for row in
                                         top_cos_genre.index.values]
    return top_cos_genre[['title', 'cosine_sim', 'genre_similarity']]


def pairwise_row_diff(df, row1, row2, column_names):
    matrix_row1 = [[df.loc[row1, cat] for cat in column_names]]
    matrix_row2 = [[df.loc[row2, cat] for cat in column_names]]
    return round(pw.cosine_similarity(matrix_row1, matrix_row2)[0][0], 5)


def remove_date_tolower_title(movie_title):
    pattern = '\(\d+\)'
    return re.compile(pattern).split(movie_title)[0].strip().lower()


def rename_columns(recommender_df):
    new_names = [remove_date_tolower_title(title) for title in recommender_df.columns]
    recommender_df.columns = new_names
