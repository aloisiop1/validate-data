# validate-data

Este projeto utiliza Quarkus

https://quarkus.io/

## Rodando sua aplicação em modo dev:
mvn quarkus:dev


> **_NOTA:_**   Dev UI em dev mode em http://localhost:8080/q/dev/.

## Empacotando e rodando a aplicação
```shell script
./mvn clean package
```

## Criando un executável nativo
```shell script
mvn package -Pnative
```


Se não possui GraalVM instalado, rode nativamente em um container usando: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Rode o executável nativo executando `./target/validate-data-1.0.0-runner`



PASSOS DE CONSTRUÇÃO DO PROJETO
-----------------------------------------------------------------------------------


1 - INSTALAR JAVA / MAVEN / DOCKER / MINIKUBE / KUBECTL
-----------------------------------------------------------------------------------
sudo snap install kubectl --classic

2 - CRIAR CONTA  E REPOSÍTÓRIO NO DOCKER HUB
-----------------------------------------------------------------------------------
https://hub.docker.com
Repositório validate-data


3 - CRIAR PROJETO COM DEPENDÊNCIAS
-----------------------------------------------------------------------------------

quarkus-resteasy-reactive-jackson	* extensão para criação de endpoints rest
quarkus-minikube			* versão simplificada do kubernetes para rodar em máquina local
quarkus-container-image-jib   		* cria imagens sem necessidade de docker file
quarkus-hibernate-validator		* extensão para validação


4 - CONSTRUIR UM PROJETO COM PELO MENOS UM ENDPOINT
-----------------------------------------------------------------------------------
.
.
.

@Path("/product")
public class ProductResource {
@Inject
Validator validator;

    //OPÇÃO 1 DE VALIDAÇÃO 
    @POST
    public Result addProduct(@Valid Product product){
        return  new Result("Produto inserido é válido");
    }

    //OPÇÃO 2 DE VALIDAÇÃO 
    @POST
    @Path("/without-validator")
    public Result addProductNoValidation(Product product){
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if(violations.isEmpty()){
            return  new Result("Produto inserido é válido");
        }else{
         return new Result(violations);
        }
    }

}

5 - RODAR/TESTAR APLCAIÇÃO QUARKUS LOCALMENTE
-----------------------------------------------------------------------------------
mvn quarkus:dev


6 - ADICIONAR CONFIGURAÇÕES NO ARQUIVO APPLICATION.PROPERTIES
-----------------------------------------------------------------------------------
quarkus.container-image.build=true
quarkus.container-image.group=a<nome-do-usuário>docker-hub


7 - CRIAR IMAGEM DA APLICAÇÃO
-----------------------------------------------------------------------------------
mvn clean package


8 - ADICIONAR IMAGEM AO REPOSITÓRIO DO DOCKER HUB
-----------------------------------------------------------------------------------
login passando usuario e senha
docker login

listar imagem criada e identificar id
docker images

criar tag
docker tag <id-da-imagem> aloisiop1/validate-data:1.0.0

enviar imagem ao docke rhub
docker push aloisiop1/validate-data:1.0.0

endereço público:
https://hub.docker.com/r/aloisiop1/validate-data/


9 - CRIAR REPOSITÓRIO GIT (GITHUB/BITBUCKET/GITLAB/ETC)
-----------------------------------------------------------------------------------
https://github.com/aloisiop1/validate-data


10 - ADICIONAR IMAGEM AO REPOSITÓRIO MINIKUBE
-----------------------------------------------------------------------------------
verificar status
minikube status

iniciar minikube se necessário
minikube start

ir para pasta kubernetes criada ao gerar a imagem do projeto
cd ~/projetos/quarkus/validate-data/target/kubernetes

rodar kubectl com os dados da imagem contidos no arquivo minikube.yml
kubectl apply -f minikube.yml

listar os serviços criados e obter o nome
kubectl get services

obter a url do  serviço criado (http://192.168.49.2:30525/)
minikube service validate-data --url

testar endpoint no postman ou usando curl
curl --location 'http://192.168.49.2:30525/product' \
--header 'Content-Type: application/json' \
--data '{
"label": "Samsung A73",    
"description": "Smartphone Samsung A73 5G",
"quantity": 1,
"price":2200.0


11 - CRIAR CONTA NO GOOGLE CLOUD OU OUTRO PROVEDOR DE NUVEM E CRIAR CLUSTER KUBERNETES
-----------------------------------------------------------------------------------   
https://cloud.google.com/

a - logar e ativar teste gratuito (se conta for nova)
b - criar um app conteinerizado (kubernetes engine)
c - ativar kubernetes engine api
d - criar cluster do kubernetes
e - conectar via gshell:
gcloud container clusters get-credentials validate-data --region us-central1 --project angelic-cat-390416
f - clonar o repostório do git
git clone git@github.com:aloisiop1/validate-data.git
g - ir para o diretório estão as confiogurações do kubernters
cd ~/validate-data/target/kubernetes
h - dar o apply do projeto via kubectl
kubectl apply -f kubernetes.yml
i - listar pods serviços e deployments
kubectl get pods
kubectl get services
kubectl get deployments
j - expor o serviço, pois até o momento apenas um ip interno está disponível (10.126.0.70 por exemplo)    
kubectl expose deployment validate-data --type=LoadBalancer --name validate-data-exposed

k - listar o serviço e verificar qual ip externo foi disponibilizado. o resultado é algo  mais o menos assim
validate-data-exposed   LoadBalancer   10.126.0.78    34.72.33.251   8080:31216/TCP,8443:30803/TCP   46s
l - testar um endpoint com o ip externo do serviço do kubernetes no google cloud:
curl --location 'http://34.72.33.251:8080/product' \
--header 'Content-Type: application/json' \
--data '{
"label": "Samsung A73",    
"description": "Smartphone Samsung A73",
"quantity": 1,
"price": -1.0
}'    
m - não se esquecer de depois de testar, remover cluster para evitar ser tarifado pelo google
