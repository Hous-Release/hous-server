# hous-infra

## description

서버 계정을 이전할 때 OS에 대한 기본 설정하는 부분을 편하게 set up 하기 위해 만든 ansible script 입니다.

ansible 은 기본적으로 ssh 연결을 통해 스크립트를 실행하는 노드에서 host 에 적힌 노드들에게 연결을 보내는 형태입니다. 즉, 컨트롤 노드에만 ansible 을 설치하여 host 연결만 해두고 스크립트를
실행하면 됩니다.

### ansible.cfg

### inventory

```
[hous-dev-server] # 제어할 노드의 이름 (playbook 파일의 hosts 에 연결되는 값)
127.0.0.1 # ip 주소
```

### playbook

- hosts: 플레이의 작업을 실행할 제어 노드를 지정
- vars: 작업 수행 시 사용할 변수를 정의
    - tasks 에서 변수 사용 시 `{{ 변수명 }}` 처럼용사용
- tasks: 실행할 작업들을 지정 (github actions 에 job)
    - tasks 는 별도의 파일로 step 만 묶어서 생성해둘 수 있으나 현재는 한 파일에 다 정의해둔태상태

## started

### 1) setting

- local pc 에 ansible 설치

```bash
# mac 의 경우
brew install ansible
ansible --version
```

- local pc 에서 ansible 을 이용해서 ssh 접속하기 위해 ssh 설정 필요

```bash
# 1. local pc 의 is_rsa.pub 키 값 복사
# 2. ec2 접속해서 ~/.ssh/authorized_keys 에 추가 
cd ~/.ssh
vim authorized_keys
```

[참고자료](https://my-studyroom.tistory.com/entry/%EB%91%90-%EA%B0%9C%EC%9D%98-EC2-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4-%EA%B0%84%EC%9D%98-SSH-%EC%84%A4%EC%A0%95%EC%9D%84-%ED%86%B5%ED%95%B4-Ansible%EB%A1%9C-%ED%86%B5%EC%8B%A0-%ED%99%95%EC%9D%B8%ED%95%98%EA%B8%B0)

### 2) execute

```bash
cd ./hous-infra
vim inventory # ip 값 수정
```

파일 실행 구문 `ansible-playbook 파일명(.yaml/.yml)`

```bash
# -v 옵션은 실행 내역을 자세히 보기 위함 
asible-playbook -v hous-server-ubuntu-setup.yaml
```

