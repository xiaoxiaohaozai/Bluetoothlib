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
	
	 compile 'com.chenhao:bluetoothlib:1.0.3'


> 最好是在Application中调用下面代码

	BluetoothUtils.init(this);
	
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

	  BluetoothUtils.getInstance().connectDevice("", new IClientListenerContract.IConnectListener() {
                        @Override
                        public void onConnectSuccess(BluetoothDevice bluetoothDevice) {
    
                        }
    
                        @Override
                        public void onConnectFailure(String msg) {
    
                        }
    
                        @Override
                        public void onConnecting() {
    
                        }
                    });
	
>4.对蓝牙当前蓝牙状态的监听
       
         BluetoothUtils.getInstance().addBlueStasusLitener(new IClientListenerContract.IBluetoothStatusListener() {
                @Override
                public void discoverStart() {
                    
                }
    
                @Override
                public void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices) {
    
                }
    
                @Override
                public void bluetoothFound(BluetoothDevice bluetoothDevice) {
    
                }
    
                @Override
                public void bluetoothOpen() {
    
                }
    
                @Override
                public void bluetoothClose() {
    
                }
    
                @Override
                public void bluetoothConnectFailure() {
    
                }
    
                @Override
                public void bluetoothConnected(BluetoothDevice bluetoothDevice) {
    
                }
    
                @Override
                public void bluetoothDisconnect() {
    
                }
            });
   
>5.发送数据

	BluetoothUtils.getInstance().sendMessage(new byte[]{},null);  
>6.接收数据(最好在发送数据之前注册)
	
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
>该库也支持对单个功能状态进行监听  

     public interface IServerStatusListener {
           void onGetClientSuccess(BluetoothDevice remoteDevice);
   
           void onGetClientFailure(String message);
       }
   
   
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
   
           void onConnecting();//正在连接中
       }
   
       /**
        * 数据接收相关
        */
       public interface IDataReceiveListener {
           void onDataSuccess(byte[] data, int length);
   
           void onDataFailure(String msg);
       }


      	