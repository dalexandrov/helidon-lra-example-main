# Helidon MicroProfile LRA example

## Online F1 booking system


Our services will be completely separated, integrated only through the REST API calls.

Additional services are needed in order to coordinate
* [lra-coordinator-service](/lra-coordinator-service) 


### Deploy to minikube
Prerequisites:
* Installed and started minikube
* Environment with
  [minikube docker daemon](https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env) - `eval $(minikube docker-env)`

#### Build images
As we work directly with
[minikube docker daemon](https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env)
all we need to do is build the docker images.
```shell
bash build.sh;
```
Note that the first build can take few minutes for all the artifacts to download.
Subsequent builds are going to be much faster as the layer with dependencies gets cached.

### Deploy to minikube
Prerequisites:
* Installed and started minikube
* Environment with
  [minikube docker daemon](https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env) - `eval $(minikube docker-env)`

#### Build images
As we work directly with
[minikube docker daemon](https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env)
all we need to do is build the docker images.
```shell
bash build.sh;
```
Note that the first build can take few minutes for all of the artifacts to download.
Subsequent builds are going to be much faster as the layer with dependencies is cached.

#### Deploy to minikube
```shell
bash deploy-minikube.sh
```
This script recreates the whole namespace, any previous state of the `f1-booking-system` is obliterated.
Deployment is exposed via the NodePort and the URL with port is printed at the end of the output:
```shell
namespace "f1" deleted
namespace/f1 created
Context "minikube" modified.
service/booking-db created
service/lra-coordinator created
service/payment-service created
service/seat-booking-service created
deployment.apps/booking-db created
deployment.apps/lra-coordinator created
deployment.apps/payment-service created
deployment.apps/seat-booking-service created
service/f1 exposed
Application f1 will be available at http://192.0.2.254:31584
```




#### Deploy to OKE
You can use the cloned helidon-lra-example repository in the OCI Cloud shell with your K8s descriptors.
Your changes are built to the images you pushed in the previous step.

In the OCI Cloud shell:
```shell
git clone https://github.com/danielkec/helidon-lra-example.git
cd helidon-lra-example
bash deploy-oci.sh

kubectl get services
```
Example output:
```shell
NAME                         TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
booking-db                   ClusterIP       192.0.2.254    <none>        3306/TCP         34s
lra-coordinator              NodePort        192.0.2.253    <none>        8070:32434/TCP   33s
oci-load-balancing-service   LoadBalancer    192.0.2.252    <pending>     80:31192/TCP     33s
payment-service              NodePort        192.0.2.251    <none>        8080:30842/TCP   32s
seat-booking-service         NodePort        192.0.2.250    <none>        8080:32327/TCP   32s
```

You can see that right after the deployment, the EXTERNAL-IP of the external LoadBalancer reads as `<pending>`
because OCI is provisioning it for you. You can invoke `kubectl get services` a little later
and see that it now gives you an external IP address with Helidon F1 example exposed on port 80.
