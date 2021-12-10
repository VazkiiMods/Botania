directory="run/crash-reports"
if [ -d $directory ]; then
  echo "Crash reports detected:"
  cat $directory/*
  exit 1
else
  echo "No crash reports detected"
  exit 0
fi
