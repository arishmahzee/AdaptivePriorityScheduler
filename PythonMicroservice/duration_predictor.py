from sklearn.linear_model import LinearRegression
import pandas as pd


class DurationPredictor:

    def __init__(self):
        self.model = None
        self.feature_columns = None

    def train(self, clean_df):
        target_column = "actual_hours"
        Y = clean_df[target_column]
        X = clean_df.drop(columns=[target_column])

       
        self.feature_columns = X.columns

        
        self.model = LinearRegression()
        self.model.fit(X, Y)

        print("Model trained on", len(clean_df), "tasks")



    def predict(self, task_features_df):
        if self.model is None:
            print("Model hasn't been trained yet!")
            return None


        task_features_df = task_features_df.reindex(
            columns=self.feature_columns,
            fill_value=0
        )

        prediction_list = self.model.predict(task_features_df)

        rounded_prediction = round(prediction_list[0], 2)

        return rounded_prediction