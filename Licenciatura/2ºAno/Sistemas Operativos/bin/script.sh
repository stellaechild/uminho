for ((i = 1; i <= 4; i++)); do
    ./client execute 15009 -u "./void 15"
done

for ((i = 1; i <= 9; i++)); do
    ./client execute 10 -u pwd
done

for ((i = 1; i <= 10; i++)); do
    ./client execute 100 -p "ls /etc | wc -l"
done

for ((i = 1; i <= 10; i++)); do
    ./client execute 100 -p "cat ../src/test.txt | grep "i" | sort -r | head -n 5 | sed s/text/TEXT/g"
done

for ((i = 1; i <= 10; i++)); do
    ./client execute 100 -p "./hello 5 | grep 1 | wc -l"
done
