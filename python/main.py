from observability.LiveData import LiveData
import time
aa = LiveData()

time.sleep(5)
print("3")
aa.addLiveData(1, "live_data, 1, 3 ,4", 0.3, "true", prediction=None)