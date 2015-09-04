#svn list -R $1 | grep \.xsd$

#regex=".*/$2/.*\.xsd$"
#echo "svn list -R $1 | grep \.xsd$"
#svn list -R svn+ssh://code.workday.com/sandbox | grep '.*/textschematestxsd/.*\.xsd$'

for var in "$@"
do
    svn list -R $var | grep \.xsd$
done
