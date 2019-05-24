## 运行


基于maven构建

数据库配置在./src/main/resources/application.properties

第一次运行需要将 spring.jpa.hibernate.ddl-auto 设置成 create

运行代码命令如下

```
mvn package
cd target
java -jar gs-rest-service-0.1.0.jar
```

一开始没有admin账号，需要自己去改数据库加或者跑以下代码

```sql
INSERT INTO `capital_account_banker` (`id`, `password`, `status`) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', '0');
```

默认帐号密码都是admin(密码存储时加密)


## API

example:

     localhost:8080/banker_login?user_id=zfx&user_password=zfx&remember_status=0

8080是端口号，根据实际命令行显示得来

### /banker_login 
- 管理员登陆
- Input:

      user_id 
      user_password
      remember_status
- Output:

      status
      message

### /banker_login_status
- 管理员是否已经登陆
- Input:
- Output:

      status
      message


### /user_find_by_banker
- 通过用户account_id获取用户信息
- Input:

      account_id
- Output:

      status
      user(password="")

### /user_add_by_banker 
- 增加一个用户
- Input:

      account_id
      password
      account_type
- Output:

      status
      message

### /user_freeze_by_banker"
- 冻结一个用户
- Input:

      account_id
- Output:

      status
      message

### /user_unfreeze_by_banker
- 解冻一个用户
- Input:

      account_id
- Output:

      status
      message

### /user_delete_by_banker
- 注销一个用户
- Input:

      account_id
- Output:

      status
      message

### /personal_user_add_by_banker
-增加一个个人用户
- Input:

      account_id
      password
      name
      gender
      id_num
      address
      job
      degree
      organization
      phone_num
      agency
      agent_id_num
- Output:

      status
      message

### /personal_user_find_by_banker
- 通过用户account_id获取个人用户信息
- Input:

      account_id
- Output:

      account_id
      name
      gender
      id_num
      address
      job
      degree
      organization
      phone_num
      agency
      agent_id_num

### /legal_user_add_by_banker
- 增加一个法人用户
- Input:

      account_id
      password
      legal_num
      license_num
      legal_name
      legal_id_num
      legal_address
      legal_phone
      authorize_name
      authorize_id_num
      authorize_address
      authorize_phone
- Output:

      status
      message

### /legal_user_find_by_banker
- 通过用户account_id获取个人法人信息
- Input:

      account_id
- Output:

      account_id
      legal_num
      license_num
      legal_name
      legal_id_num
      legal_address
      legal_phone
      authorize_name
      authorize_id_num
      authorize_address
      authorize_phone

### /user_login
- 用户登陆
- Input:

      account_id 
      password
      remember_status
- Output:

      status
      message

### /user_login_status 
- 用户是否已经登陆
- Input:
- Output:

      status
      message