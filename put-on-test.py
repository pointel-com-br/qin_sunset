#!/usr/bin/env python3

import os
import shutil

qin_root = os.environ['QIN_ROOT']
os.system("mvn clean install")
shutil.copytree("./dist", f"{qin_root}/Test", dirs_exist_ok=True)
shutil.copy("./start.bat", f"{qin_root}/Test/start.bat")
shutil.copy("./start.sh", f"{qin_root}/Test/start.sh")
