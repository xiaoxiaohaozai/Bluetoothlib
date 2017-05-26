BluetoothLib  
====
*该库基于SPP协议（Serial Port Profile）开发，主要用于Android设备与蓝牙模块串口通信*
功能表述：  
------
>支持蓝牙模块的扫描  
>支持Android设备和蓝牙模块之间的快速连接  
>支持串口数据的接收和发送  
>对Android设备的部分蓝牙状态的监听  
>支持Ble模块扩展（正在完善中...）  
>提供一套默认蓝牙功能界面以便使用  

简单使用  
------
1.初始化  
>添加依赖  
	
	 compile 'com.chenhao:bluetoothlib:1.0.1'


> 最好是在Application中调用下面代码

	BluetoothUtils.init(this);

	
>在结束时调用

	BluetoothUtils.getInstance().onDestroy();  
	
>获得实例  

	BluetoothUtils instance = BluetoothUtils.getInstance();
	
2.功能
>1.打开或关闭蓝牙

	instance.openBluetooth(null); 
	instance.closeBluetooth(null); 
	
>2.开启或结束扫描

	instance.searchBluetoothDevices(null);
	instance.cancelBluetoothSearch();
	
>3.连接蓝牙模块

	instance.connectDevice(adress, null);  
	
>4.对蓝牙当前蓝牙状态的监听
	
	   BluetoothUtils.getInstance().addBlueStasusLitener(new IClientListenerContract.IBluetoothStatusListener() {
            @Override
            public void discoverStart() {
            //扫描开始
            }

            @Override
            public void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices) {
            //扫描结束
            }

            @Override
            public void bluetoothFound(BluetoothDevice bluetoothDevice) {
            //发现一个设备
            }

            @Override
            public void bluetoothOpen() {
             //蓝牙打开
            }

            @Override
            public void bluetoothClose() {
             //蓝牙关闭
            }

            @Override
            public void bluetoothConnected(BluetoothDevice bluetoothDevice) {
             //蓝牙连接
            }

            @Override
            public void bluetoothDisconnect(BluetoothDevice bluetoothDevice) {
             //蓝牙断开
            }
        });
    }
    
>5.发送数据

	BluetoothUtils.getInstance().sendMessage(new byte[]{},null);  
>6.接收数据
	
	    BluetoothUtils.getInstance().recevieMessage(new IClientListenerContract.IDataReceiveListener() {
            @Override
            public void onDataSuccess(byte[] data, int length) {
                //接收数据成功
            }

            @Override
            public void onDataFailure(String msg) {
               //接收数据失败
            }
        });	
3.扩展
>该库是支持添加自定义蓝牙模块，例如ble模块  
>默认时标准蓝牙模块

	 BluetoothUtils.getInstance().bindBlueModule(ClassicBluetoothModule.newInstance(this)); 
	 
>如果需要扩展请实现
		
	ICommonBTModule接口，具体看Demo
>该库也支持对单个功能状态进行监听  

    /**
     * 寻找设备相关
     */
    public interface ISearchDeviceListener {
        void onSearchStart();

        void onFindDevice(BluetoothDevice bluetoothDevice);

        void onSearchEnd(List<BluetoothDevice> bluetoothDevices);
    }

    /**
     * 连接相关
     */
    public interface IConnectListener {
        void onConnectSuccess(BluetoothDevice bluetoothDevice);

        void onConnectFailure(String msg);
    }

    /**
     * 数据接收相关
     */
    public interface IDataReceiveListener {
        void onDataSuccess(byte[] data, int length);

        void onDataFailure(String msg);
    }

    /**
     * 数据发送监听
     */
    public interface IDataSendListener {
        void onDataSendSuccess(byte[] data);

        void onDataSendFailure(String msg);
    }


    /**
     * 蓝牙打开成功或关闭
     */
    public interface IBlueClientIsOpenListener {
        void onOpen();

        void onClose();
    }
4.补充  
>如果需要的话，本库中提供了一套简单的蓝牙功能界面以便使用，直接添加fragment就行了  
>例如：  
	
	    BlueFragment blueFragment = BlueFragment.newInstance();                  
	    getSupportFragmentManager().beginTransaction().replace(R.id.fl,blueFragment).commit();
        blueFragment.setOnUserActionListener(new BlueFragment.OnUserActionListener() {
            @Override
            public void onBack() {//添加返回监听
 
            }

            @Override
            public void onHandleIntent(BluetoothDevice bluetoothDevice) {//处理事件

            }
        });

      	
![github](https://github.com/xiaoxiaohaozai/Bluetoothlib/blob/master/img/test.jpg?raw=true)
