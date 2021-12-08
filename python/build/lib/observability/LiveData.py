# -*- coding:utf-8 -*-
import datetime
from observability.Wocker import Worker


class LiveData:

    def __init__(self):
        self.dt_start = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    def addLiveData(self, model_id, live_data, confidence, target=None, prediction=None):
        dt_start = self.dt_start
        dt_end = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        diff = self.diff(dt_start, dt_end)
        th = Worker(model_id=model_id, live_data=live_data, confidence=confidence, target=target, prediction=prediction,
                    dt_start=dt_start, dt_end=dt_end, processing_time=diff.seconds)
        th.start()  # run()에 구현한 부분이 실행된다

    def diff(self, dt_start, dt_end):
        return datetime.datetime.strptime(dt_end, '%Y-%m-%d %H:%M:%S') - datetime.datetime.strptime(dt_start, '%Y-%m-%d %H:%M:%S')
