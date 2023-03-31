#!/bin/bash
# Insert all people who have worked on each file in com.autovend.software*
find com.autovend.software* -name '*.java' -print0 | while read -r -d $'\0' file; do
  echo "editing $file: "
  # git blame "$file" -p | grep "^author " | sed 's/^/@/' | uniq | sort -u
  printf "\n"
  cat authors.txt "$file" > temp && mv temp "$file"
done
