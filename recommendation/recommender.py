import pandas as pd
import re


def item_based_recom(recommender_df, film_name):
    ## Item Rating Based Cosine Similarity
    film_name = remove_date_tolower_title(film_name)
    cosine_df = pd.DataFrame(recommender_df[film_name].sort_values(ascending=False))
    cosine_df.reset_index(level=0, inplace=True)
    cosine_df.columns = ['title', 'cosine_sim']
    return cosine_df


def remove_date_tolower_title(movie_title):
    pattern = '\(\d+\)'
    return re.compile(pattern).split(movie_title)[0].strip().lower()


def rename_columns(recommender_df):
    new_names = [remove_date_tolower_title(title) for title in recommender_df.columns]
    recommender_df.columns = new_names
