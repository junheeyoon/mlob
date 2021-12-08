import threading
import requests
import json
import observability

class Worker(threading.Thread):
    """클래스 생성시 threading.Thread를 상속받아 만들면 된다"""

    def __init__(self, model_id, live_data, confidence, dt_start, dt_end, processing_time, target, prediction):
        """__init__ 메소드 안에서 threading.Thread를 init한다"""
        threading.Thread.__init__(self)
        self.model_id = model_id
        self.live_data = live_data
        self.confidence = confidence
        self.dt_start = dt_start
        self.dt_end = dt_end
        self.processing_time = processing_time
        self.target = target
        self.prediction = prediction

    def run(self):
        """start()시 실제로 실행되는 부분이다"""
        API_HOST = observability.API_HOST
        url = API_HOST + observability.URL
        headers = {'Content-Type': 'application/json', 'charset': 'UTF-8', 'Accept': '*/*'}
        body = {
            "modelId": self.model_id,
            "liveData": self.live_data,
            "confidence": self.confidence,
            "dtStart": self.dt_start,
            "dtEnd": self.dt_end,
            "processingTime": self.processing_time,
            "target": self.target,
            "prediction": self.prediction
        }

        try:
            response = requests.post(url, headers=headers, data=json.dumps(body, ensure_ascii=False, indent="\t"))
            print("response status %r" % response.status_code)
            print("response text %r" % response.text)
        except Exception as ex:
            print(ex)
