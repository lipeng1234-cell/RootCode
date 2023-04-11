#!/bin/bash

# 所有机器实例都需要执行的脚本文件

# 安装docker

# 先删除安装过的docker
yum remove docker*

# 配置yum 源
yum install -y yum-utils
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 安装docker
# yum install -y docker-ce docker-ce-cli containerd.io
# 安装指定版本
yum install -y docker-ce-19.03.5 docker-ce-cli-19.03.5  containerd.io-1.4.6

sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://inpq4kkk.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload
sudo systemctl restart docker
#############################################################################

# 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

#关闭swap
swapoff -a
sed -ri 's/.*swap.*/#&/' /etc/fstab

#允许 iptables 检查桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF

cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sudo sysctl --system

# 安装kubelet、kubeadm、kubectl
cat << EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
   http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
exclude=kubelet kubeadm kubectl
EOF

sudo yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9 --disableexcludes=kubernetes

# 启动并开机启动
sudo systemctl enable --now kubelet

# 查看kubelet 需要哪些镜像
# kubeadm config images list
# [root@iZ0jl2obrmiiazpjnm659tZ ~]# kubeadm config images list
# I0328 21:59:29.124088    9999 version.go:254] remote version is much newer: v1.26.3; falling back to: stable-1.20
# k8s.gcr.io/kube-apiserver:v1.20.15
# k8s.gcr.io/kube-controller-manager:v1.20.15
# k8s.gcr.io/kube-scheduler:v1.20.15
# k8s.gcr.io/kube-proxy:v1.20.15
# k8s.gcr.io/pause:3.2
# k8s.gcr.io/etcd:3.4.13-0
# k8s.gcr.io/coredns:1.7.0

# 下载k8s 所需要的基本镜像

images=(
kube-apiserver:v1.20.9
kube-proxy:v1.20.9
kube-controller-manager:v1.20.9
kube-scheduler:v1.20.9
coredns:1.7.0
etcd:3.4.13-0
pause:3.2
)

for imageName in ${images[@]} ; do
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/$imageName
done