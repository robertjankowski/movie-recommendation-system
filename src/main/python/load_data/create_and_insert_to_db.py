import pandas as pd

songs = pd.read_csv('song_data.csv')
df = pd.read_csv('songs.txt', sep='\t', header=None)
df.columns = ["user_id", "song_id", "listen_time"]


def SQL_CREATE_STATEMENT_FROM_DATAFRAME(SOURCE, TARGET):
    # SOURCE: source dataframe
    # TARGET: target table to be created in database
    return pd.io.sql.get_schema(SOURCE.reset_index(), TARGET)


def SQL_INSERT_STATEMENT_FROM_DATAFRAME(SOURCE, TARGET):
    sql_texts = []
    for index, row in SOURCE.iterrows():
        sql_texts.append(
            'INSERT INTO ' + TARGET + ' (' + str(', '.join(SOURCE.columns)) + ') VALUES ' + str(tuple(row.values)))
    return sql_texts


with open('001_data.sql', 'w', encoding='utf-8') as f:
    f.write(SQL_CREATE_STATEMENT_FROM_DATAFRAME(df, 'data'))
    f.write('\n')
    f.write(SQL_CREATE_STATEMENT_FROM_DATAFRAME(songs, 'songs'))
    f.write('\n')
    f.write('\n'.join(SQL_INSERT_STATEMENT_FROM_DATAFRAME(df, 'data')))
    f.write('\n'.join(SQL_INSERT_STATEMENT_FROM_DATAFRAME(songs, 'songs')))
