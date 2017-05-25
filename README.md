#食品管理库集成
该库主要用于对冰箱中食品按新鲜度和储藏室位置进行分类
####功能描述： 
>支持实时更新食品的新鲜度状态  
>支持弹窗显示当前食品相关信息  
>支持批量删除  
>支持自定义添加和选择内置食品添加

##开始集成
###1.准备工作  
>本库采用lib的方式导入，只需在项目中对，提供的**lib_fm**库添加本地依赖即可. 

###2.配置参数
>本库主要包含三个核心组件  
> 1.FridgeManagerActivity 食品管理  
> 2.FoodAddActivity 食品添加  
> 3.FoodManagerService 食品状态更新  


####配置说明:  
  >在AndroidManifest声明以下代码
	
    <activity android:name="com.chenhao.fm.foodsadd.FoodAddActivity"></activity>
    <activity android:name="com.chenhao.fm.foodshow.FridgeManagerActivity"><activity>
    <service android:name="com.chenhao.fm.service.FoodManagerService"></service>
    下面两个是用于适配相关的也必须加上，否者会抛出异样
    <meta-data
       android:name="design_width"
       android:value="1280"></meta-data>
    <meta-data
       android:name="design_height"
       android:value="800"></meta-data>
       
>在application中初始化 

	FoodManngerContext.getFoodManngerContext(this).init();

 
###3.使用  
>在需要跳转的界面使用以下代码即可   

     FridgeManagerActivity.showFridgeManagerActivity(this);	
###集成完毕

