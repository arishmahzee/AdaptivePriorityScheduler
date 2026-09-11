from duration_predictor import DurationPredictor
import pandas as pd


class PredictionService:

    def __init__(self):
        self.predictor = DurationPredictor()
        self.is_trained = False

    def train_model(self, clean_df):
       
        self.predictor.train(clean_df)
        self.is_trained = True

    def predict(self, task_data):
        if self.is_trained == False:
            print("Warning: model not trained yet, returning raw effort")
            fallback_hours = task_data.get("hoursNeeded", 0)
            return fallback_hours

        hours_needed = task_data.get("hoursNeeded", 0)
        start_pct_of_slack = task_data.get("startPctOfSlack", 0)
        category = (task_data.get("category", "unknown")).lower()
     

      
        task_df = pd.DataFrame([{
            "planned_hours": hours_needed,
            "start_pct_of_slack": start_pct_of_slack,
        }])

        # turn category into the same one-hot columns training used
        category = task_data.get("category", "unknown").lower()
        task_df[f"cat_{category}"] = 1

        prediction = self.predictor.predict(task_df)

        return prediction