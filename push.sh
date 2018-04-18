# GIT快速提交命令
#!/bin/sh
git add -A
default_commit_message="update"
commit_message=$1
: ${commit_message:=$default_commit_message}
git commit -m "${commit_message}"
branch_name=`git describe --contains --all HEAD|tr -s '\n'`
git push origin ${branch_name}