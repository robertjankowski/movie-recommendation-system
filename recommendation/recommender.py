import pandas as pd


def item_based_recom(recommender_df, film_name):
    ## Item Rating Based Cosine Similarity
    cosine_df = pd.DataFrame(recommender_df[film_name].sort_values(ascending=False))
    cosine_df.reset_index(level=0, inplace=True)
    cosine_df.columns = ['title', 'cosine_sim']
    return cosine_df
