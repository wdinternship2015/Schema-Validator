echo "run export with new process"
/bin/bash $1exportSchemaScript.sh &
child_pid=$!
echo "child_pid/$child_pid/"