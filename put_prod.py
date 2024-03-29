import os
import shutil

qin_root = os.environ['QIN_ROOT']
os.system("mvn clean install")
shutil.copytree("./dist", f"{qin_root}/Prod", dirs_exist_ok=True)
shutil.copy("./setup-prod.json", f"{qin_root}/Prod/setup.json")
shutil.copy("./start.bat", f"{qin_root}/Prod/start.bat")
shutil.copy("./start.sh", f"{qin_root}/Prod/start.sh")
