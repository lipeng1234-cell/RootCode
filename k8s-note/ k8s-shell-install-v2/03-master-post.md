

## 【仅master 节点】安装k8s-dashboard

1. 安装`recommended`

   > kubectl apply -f k8s-yaml/recommended.yaml

2. 暴露端口

   > `type: ClusterIP` 改为 `type: NodePort`

   ```bash
   kubectl edit svc kubernetes-dashboard -n kubernetes-dashboard
   ```

3. 安全组放行

   > 查看端口

   ```bash
   [root@k8s-master ~]# kubectl get svc -A |grep kubernetes-dashboard
   kubernetes-dashboard   dashboard-metrics-scraper   ClusterIP   10.96.234.110   <none>        8000/TCP                 6m18s
   kubernetes-dashboard   kubernetes-dashboard        NodePort    10.96.179.151   <none>        443:30499/TCP            6m18s
   ```

4. 令牌

   > 创建令牌

   ```bash
   kubectl apply -f dash-user.yaml
   ```

   > 获取令牌

   ```bash
   kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
   ```

   > 使用令牌去登录

   ```txt
   eyJhbGciOiJSUzI1NiIsImtpZCI6ImlZXzNXV3o1ZEhJaVNoejJ5ckZGYlEtaTJTeEtKM0FzYWJNR0M5cXlyejAifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLXhza3JuIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlNjY1YmRkMy1jOTQ4LTRmZjctODM4ZC1lZWE0N2Y5ZGNlMTIiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.CXdmuraflALTcKvfUz-EwElKLkwqo_Wopm2elJBKNlRp2LTR4ah6jjghoabIgpv1pp_sZoA6l4h0yeTRGRengfa-rLQoxLFnKUMSQ86FbysPQ1pE-Q5vpKaX30lU4EJOmI0PeliVOA1jIfJiCVyTXtj1PEw6cpuhh4JJd0eFtzOdyLBmjYOzKYCqmq1Gzaea5uP2kxy9v8EQet9UsayQ7FMYlRZaM2HJXMkcSDD_EfS7kRkjSDvT7kVYWLCSzaCC-NH3XVautnTmf3W-GdhXRTlFKQ8WSC5d0YXBAwJtPC0G1Rz_9ss1aB_p2IUoc1yMGjFOzXjWjzFiWUjPh0qDzQ
   ```

5. 登录

   > 访问需要使用 https://IP:PORT

6. 其他



```shell
# 安装ingress
# 它需要安装在某一个节点上面
kubectl apply -f k8s-yaml/ingress.yaml


# 安装bash 的命令自动补全功能
yum install bash-
echo 'source <(kubectl completion bash)' >> ~/.bashrc
kubectl completion bash > /etc/bash_completion.d/kubectl
source /usr/share/bash-completion/bash_completion
```