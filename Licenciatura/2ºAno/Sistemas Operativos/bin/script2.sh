for ((i = 1; i <= 10; i++)); do
    ./client execute 3000 -u "./void 3"
done

for ((i = 1; i <= 10; i++)); do
    ./client execute 2000 -u "./void 2"
done

for ((i = 1; i <= 10; i++)); do
    ./client execute 1000 -u "./void 1"
done

for ((i = 1; i <= 50; i++)); do
    ./client execute 1000 -p "./hello 1 | grep 1 | wc -l"
done

for ((i = 1; i <= 20; i++)); do
    ./client execute 3000 -p "./hello 3 | grep 1 | wc -l"
done