import subprocess
import time

tool_excutable = "./purity_infer.sh"
start = time.time()
rtn_code = subprocess.call([tool_excutable], shell=True) 
end = time.time()
print "Return code is {}.".format(rtn_code)
print "Time taken by purity checking: \t{}\t seconds".format(end - start)
