from flask import Flask, request, jsonify
from prediction_service import PredictionService
from generate_fake_data import generate_fake_data
from data_preprocessor import DataPreprocessor

app = Flask(__name__)
prediction_service = PredictionService()


raw_df = generate_fake_data(50)

preprocessor = DataPreprocessor()
clean_df = preprocessor.clean(raw_df)

prediction_service.train_model(clean_df)



@app.post("/predict-duration")
def predict_duration():
    task_data = request.get_json()

    
    if task_data is None:
        return jsonify({"error": "No data was sent"}), 400

    if "hoursNeeded" not in task_data:
        return jsonify({"error": "hoursNeeded required"}), 400

    result = prediction_service.predict(task_data)

    return jsonify({"predictedHours": result})


if __name__ == "__main__":
    app.run(port=5000, debug=True)