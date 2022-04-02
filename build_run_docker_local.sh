./gradlew build
sudo docker build -t ledger .
sudo docker run -dp 8080:8080 ledger