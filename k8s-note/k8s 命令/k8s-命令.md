

```bash
# 打标签
# kubectl label --help
kubectl label node k8s-node01 node-role.kubernetes.io/worker='abc'
```

```bash
# 查看标签
[root@iZ0jl2obrmiiazpjnm659tZ ~]# kubectl get node 
NAME         STATUS   ROLES                  AGE   VERSION
k8s-master   Ready    control-plane,master   38m   v1.20.9
k8s-node01   Ready    worker                 34m   v1.20.9

```



## 设置ipvs 模式

k8s 整个集群为了访问能通；默认是用iptables，性能下降(kube-proxy 在集群之间同步iptables的内容)



```bash
#1、查看默认kube-proxy 使用的模式
# kubectl logs -n kube-system kube-proxy-28xv4
#2、需要修改 kube-proxy 的配置文件,修改mode 为ipvs。默认iptables，但是集群大了以后就很慢
kubectl edit cm kube-proxy -n kube-system
# 修改如下
   ipvs:
      excludeCIDRs: null
      minSyncPeriod: 0s
      scheduler: ""
      strictARP: false
      syncPeriod: 30s
    kind: KubeProxyConfiguration
    metricsBindAddress: 127.0.0.1:10249
    # 关键点，就修改它
    mode: "ipvs"
 ###修改了kube-proxy的配置，为了让重新生效，需要杀掉以前的Kube-proxy
[root@iZ0jl2obrmiiazpjnm659tZ ~]# kubectl get pod -A | grep kube-proxy
kube-system            kube-proxy-dzsxg                             1/1     Running   0          46m
kube-system            kube-proxy-kg8wc                             1/1     Running   0          42m
# 使用如下命令杀掉pod
 kubectl delete pod kube-proxy-dzsxg -n kube-system
 kubectl delete pod kube-proxy-kg8wc -n kube-system
### 修改完成后可以重启kube-proxy以生效
```



```bash 
kubectl get all
```

