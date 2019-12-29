docker build -t mac_project/neo4j .
docker run -p7474:7474 -p7687:7687 -v $PWD/data:/data -v $PWD/import:/var/lib/neo4j/import -v $PWD/plugins:/plugins --env NEO4J_AUTH=neo4j/test mac_project/neo4j
