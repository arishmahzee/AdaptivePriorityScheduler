package AdaptivePriorityScheduler.pyAPI;

import AdaptivePriorityScheduler.tasks.Task;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Component
public class PythonClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonUrl = "http://localhost:5000/predict-duration";

    public double callPredictDuration(Task task) {
        Map<String, Object> body = new HashMap<>();
        body.put("hoursNeeded", task.getHoursNeeded());

        Map response = restTemplate.postForObject(pythonUrl, body, Map.class);
        return (double) response.get("predictedHours");
    }
}

