import numpy as np
import pandas as pd


class DataPreprocessor():

    def clean(self, raw_df):
        df = raw_df.copy()


        df.columns = (
            df.columns
            .str.strip()
            .str.lower()
            .str.replace(" ", "_", regex=False)
        )


        if "category" in df.columns:
            df["category"] = (
                df["category"]
                .astype("string")
                .str.strip()
                .str.lower()
            )


        numeric_cols = ["planned_hours", "actual_hours", "start_pct_of_slack"]
        for col in numeric_cols:
            if col in df.columns:
                df[col] = pd.to_numeric(df[col], errors="coerce")


        required_cols = []
        for col in ["planned_hours", "actual_hours"]:
            if col in df.columns:
                required_cols.append(col)
        df = df.dropna(subset=required_cols)
        df = df.copy()


        df = df.drop_duplicates().copy()



        for col in ["planned_hours", "actual_hours"]:
            if col in df.columns:
                df = df[df[col] >= 0]



        if "category" in df.columns:
            df = pd.get_dummies(df, columns=["category"], prefix="cat")

        df = df.reset_index(drop=True)

        return df
        