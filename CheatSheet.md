## Prepare minikube
```shell
minikube start
# Use minikube's docker registry
eval $(minikube docker-env)
```

## Investigate
```shell
# Set current namespace
kubectl config set-context --current --namespace=f1
# List all pods
kubectl get pods
# List all services
kubectl get svc

# Inspect F1 booking service
kubectl logs --tail 100 -l app=f1-booking-service
kubectl describe pod -l app=f1-booking-service

# Inspect seat booking service
kubectl logs --tail 100 -l app=f1-seat-booking-service
kubectl describe pod -l app=f1-seat-booking-service

# Inspect payment service
kubectl logs --tail 100 -l app=f1-payment-service
kubectl describe pod -l app=f1-payment-service

# Inspect coordinator service
kubectl logs --tail 100 -l app=lra-coordinator
kubectl describe pod -l app=lra-coordinator
```