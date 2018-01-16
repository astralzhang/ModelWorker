/*
 * writeboard
 * @Author toubidu
 * @param {arg}      obj       顶层对象，
 * @param {arg}      x         横坐标数组
 * @param {arg}      y		   纵坐标数组
 * @param {arg}      n		   笔画状态
 * @param {arg}      t		   时间间隔
 * @param {arg}      callback  回调，返回x，y，n，t，图片路径
 * @param {arg}      type	   题型
 * @param {arg}      content   容器内容，临摹文字，拼音等
 */
function writeboard(obj,x,y,n,t,callback,type,content, index){//init fun  参数：顶级对象 x坐标 y坐标 笔画标识 时间差 回调定义 临摹文字
	//init data
	//var backcanvas = obj.find(".backcanvas")[0];  //获取canvas   
	//var backcontext = backcanvas.getContext('2d');  //canvas追加2d画图
	var canvas = obj.find("canvas")[0];  //获取canvas   
	var context = canvas.getContext('2d');  //canvas追加2d画图
	//var realcanvas = obj.find(".realcanvas")[0];  //获取canvas   
	//var realcontext = realcanvas.getContext('2d');  //canvas追加2d画图
	var canvasimg=new Image();
	var winHeight = $("#myModal5").height();
	var winWidth = $("#myModal5").width();
	var btnWidth = 100;
	var btnHeight = 200;
	//backcanvas.height=winHeight;
	//backcanvas.width=winWidth - btnWidth;
	canvas.height=winHeight - btnHeight;
	canvas.width=winWidth - btnWidth;
	//realcanvas.height=winHeight;
	//realcanvas.width=winWidth - btnWidth;
	//$(".writeboard-box").css("width", winWidth - btnWidth);
	//$(".writeboar-btn").css("left", winWidth - btnWidth);
	//obj.css('height',1000);
	//obj.find(".writeboard-box").css('height',winHeight - 100);
	//obj.find(".writeboar-btn").css('height',winHeight - 100);
	//canvasimg.src="tzg.gif";			
/*	canvasimg.onload=function(){
		backcontext.drawImage(canvasimg,0,0,300,300);
		backcontext.fillStyle="#eeeeee";
		backcontext.textAlign = 'center';
   		backcontext.font = "240px 宋体";
    		backcontext.fillText(content.font,150,240);
		backcontext.strokeStyle="#add59e";
		backcontext.strokeText(content.font,150,240);										  				  			  
  	};*/
	context.lineCap="round";
	//end
	var lastX = -1;  //路径坐标，从起始到下一个的坐标 
	var lastY = -1;  //路径坐标
	var flag = 0;  //标志，判断是按下后移动还是未按下移动 重要
	var penwidth=10; //画笔宽度
	var pencolor="#000";//画笔颜色
	var timecha=80;//笔画间隔时间差
	var imageurl;//存放canvas 图片base64 路径
	var linex = new Array();  //横坐标
	var liney = new Array();  //纵坐标
	var linen = new Array();  //移动坐标
	var linetime = new Array();  //记录时间
	if(x&&y&&n&&t){ //数据外加载
		linex = x;  //横坐标
		liney = y;  //纵坐标
		linen = n;  //移动坐标
		linetime = t;  //记录时间
		strokel();
	}else{
		linex = [];  //横坐标
		liney = [];  //纵坐标
		linen = [];  //移动坐标
		linetime = [];  //记录时间
	};
	//handle
	canvas.addEventListener('mousemove', onMouseMove, false); //鼠标移动事件 
	canvas.addEventListener('mousedown', onMouseDown, false);  //鼠标按下事件 
	canvas.addEventListener('mouseup', onMouseUp, false);  //鼠标抬起事件 
	document.documentElement.addEventListener('mouseup', ondocMouseMove, false);//防止冒泡	
	function ondocMouseMove(evt) {
		if(evt.target.tagName=="CANVAS"){		
		}else{
			if(flag == 1) {
			  flag = 0;  
			  linex.push(evt.layerX);  
			  liney.push(evt.layerY);  
			  linen.push(0);  
			  linetime.push(new Date().getTime());	
			}else{
				
			};	
		};	
	};	
	function onMouseMove(evt) {	
	  if (flag == 1) { 
			 linex.push(evt.layerX);  //坐标存入数组
			 liney.push(evt.layerY);  
			 linen.push(1);  //移动1位
			 linetime.push(new Date().getTime());//步骤和记录的时间戳
			 //console.log(linen)
			 strokel();		
	  }		    
	}; 		 
	function onMouseDown(evt) {  
	  flag = 1;  //标志按下
	  linex.push(evt.layerX);  //坐标存入数组   layer获取相对于当前元素的坐标，不同于pagex获取相对页面
	  liney.push(evt.layerY);  
	  linen.push(0);  //按下0位
	  linetime.push(new Date().getTime());
	}  
	function onMouseUp(evt) {  
	  flag = 0;  
	  linex.push(evt.layerX);  
	  liney.push(evt.layerY);  
	  linen.push(0);  
	  linetime.push(new Date().getTime());	    
	} 
	function strokel(){ //路径绘制
		context.clearRect(0,0,canvas.width,canvas.height);	
		context.save();  //存储当前画布状态，破坏以前
		context.translate(context.canvas.width/2, context.canvas.height/2);  //原点从0,0移动到中心150,150
		context.translate(-context.canvas.width/2, -context.canvas.height/2);  //原点从150,150移动到0,0
		context.beginPath();  //开始绘制路径				
		for (var i=0;i<linex.length;i++) {  //移动鼠标，这个数组会不断push插入1，计算移动次数
			 lastX = linex[i];  //移动坐标
			 lastY = liney[i];  
			 context.lineWidth = penwidth;  //线宽度
			 context.strokeStyle = pencolor;  
			 if (linen[i] == 0) {  //刚按下那个坐标位置，移动开始前			
				context.moveTo(lastX,lastY);  //绘制路径的起始点坐标
				//context.stroke();  //绘制
			 } else {  //发生移动
				context.lineTo(lastX,lastY);  //绘制以后的左边点
				//context.stroke();  //绘制
			 };  
		};    
		context.stroke();  //绘制		
		context.restore();  //释放画布以前状态	
	};
	//回放 处理
	var movPlayBtn = obj.find(".movPlayBtn");
	movPlayBtn.click(movPlay);	
	var settimea=[];
	function movPlay () {		
		//console.log(linen)
		for(var i=0;i<settimea.length;i++){
			clearTimeout(settimea[i]);
		};
		context.clearRect(0,0,canvas.width,canvas.height);		
		context.save(); 
		context.translate(context.canvas.width/2, context.canvas.height/2);  
		context.translate(-context.canvas.width/2, -context.canvas.height/2); 
		context.beginPath();  		
		var datestart=0;
		var funa=[];
		for (var i=0;i<linen.length;i++) {				
			if(i==0){		
				context.lineWidth = penwidth;  //线宽度	
				context.strokeStyle = pencolor;  
				context.moveTo(linex[0],liney[0]);				
				context.stroke();  //绘制	
				
			}else{
				if(linen[i]==0 && linen[i-1]==0){				
					datestart=parseInt(datestart)+parseInt(linetime[i]-linetime[i-1])<timecha ? parseInt(datestart)+parseInt(linetime[i]-linetime[i-1]):parseInt(datestart)+parseInt(timecha);			
					if (linen[i] == 0) { 			
						(funa[i]=function(datestart,xx,yy){
							settimea[i]=setTimeout(function(){
							context.clearRect(0,0,canvas.width,canvas.height);	
							context.lineWidth = penwidth;  //线宽度	
							context.strokeStyle = pencolor;  
							context.moveTo(xx,yy);							
							context.stroke();  //绘制
							},datestart)
						})(datestart,linex[i],liney[i]);		
					} else { 
						(funa[i]=function(datestart,xx,yy){
							settimea[i]=setTimeout(function(){
							context.clearRect(0,0,canvas.width,canvas.height);	
							context.lineWidth = penwidth;  //线宽度
							context.strokeStyle = pencolor;  	
							context.lineTo(xx,yy);							
							context.stroke();  //绘制
							},datestart)
						})(datestart,linex[i],liney[i]);					
					}; 			
					
				}else{
					datestart=parseInt(datestart)+parseInt(linetime[i]-linetime[i-1]);			
					if (linen[i] == 0) { 			
						(funa[i]=function(datestart,xx,yy){
							settimea[i]=setTimeout(function(){
							context.clearRect(0,0,canvas.width,canvas.height);	
							context.lineWidth = penwidth;  //线宽度	
							context.strokeStyle = pencolor;  
							context.moveTo(xx,yy);							
							context.stroke();  //绘制
							},datestart)							
						})(datestart,linex[i],liney[i]);		
					} else { 
						(funa[i]=function(datestart,xx,yy){
							settimea[i]=setTimeout(function(){
							context.clearRect(0,0,canvas.width,canvas.height);	
							context.lineWidth = penwidth;  //线宽度	
							context.strokeStyle = pencolor;  
							context.lineTo(xx,yy);							
							context.stroke();  //绘制
							},datestart)
						})(datestart,linex[i],liney[i]);					
					}; 			
	
				};
				context.stroke();  //绘制
			}
									
		}	
		context.restore();  //释放画布以前状态，不能写字就破坏了		       
	};		
	//重置 处理
	var resetCanvasBtn = $("#btn-redraw");//obj.find("#btn-redraw");	 
	resetCanvasBtn.click(resetCanvas);	
	function resetCanvas () {
		context.clearRect(0,0,canvas.width,canvas.height);
		linex = [];  //横坐标
		liney = [];  //纵坐标
		linen =[];  //移动坐标 
		linetime =[];  //时间戳 
		lastX = -1;  //路径坐标，从起始到下一个的坐标 
		lastY = -1;  //路径坐标
		flag = 0;  //标志，判断是按下后移动还是未按下移动 重要	
	};
	//回退 处理
	var goBackBtn = obj.find(".goBackBtn");	 
	goBackBtn.click(goBack);	
	function goBack () {			
		var linenlastIndex=linen.join("").substr(0,linen.length-1).lastIndexOf("0");
		if(linenlastIndex==0){
			resetCanvas();
		}else{
			linex = linex.slice(0,linenlastIndex);  //记录值-1
			liney = liney.slice(0,linenlastIndex);  //纵坐标
			linen =linen.slice(0,linenlastIndex);  //移动坐标 
			linetime=linetime.slice(0,linenlastIndex);
			//console.log(linen)
			strokel();
		}
	};
	//事件绑定
	function bindButtonEvent(element, type, handler) 
	{ 
		if(element.addEventListener) { 
		element.addEventListener(type, handler, false); 
		} else { 
		element.attachEvent('on'+type, handler); 
		} 
	};	
	//确定事件 保存处理
	/*function strokelreal(){ //路径绘制	
		realcontext.save();  //存储当前画布状态，破坏以前
		realcontext.translate(context.canvas.width/2, context.canvas.height/2);  //原点从0,0移动到中心150,150
		realcontext.translate(-context.canvas.width/2, -context.canvas.height/2);  //原点从150,150移动到0,0
		realcontext.beginPath();  //开始绘制路径				
		for (var i=0;i<linex.length;i++) {  //移动鼠标，这个数组会不断push插入1，计算移动次数
			 lastX = linex[i];  //移动坐标
			 lastY = liney[i];  
			 realcontext.lineWidth = penwidth;  //线宽度
			 realcontext.strokeStyle = pencolor;  
			 if (linen[i] == 0) {  //刚按下那个坐标位置，移动开始前			
				realcontext.moveTo(lastX,lastY);  //绘制路径的起始点坐标
				//context.stroke();  //绘制
			 } else {  //发生移动
				realcontext.lineTo(lastX,lastY);  //绘制以后的左边点
				//context.stroke();  //绘制
			 };  
		};    
		realcontext.stroke();  //绘制		
		realcontext.restore();  //释放画布以前状态	
	};*/
	var oksave = $("#btn-sign-confirm");obj.find(".mar-2");	 
	oksave.click(gosave);	
	function gosave() {
		var imgdata = canvas.toDataURL();
		$("#processContent" + index).html("");
		$('<img src="' + imgdata + '" height="100">').appendTo($("#processContent" + index));
		$('#myModal5').modal("hide");
		/*MessageBox.processStart();
		$.ajax({
			url:"/flow/flowProcessor/saveSign",
			type:"post",
			dataType:"json",
			data:{data:imgdata},
			success:function (data) {
				alert(index);
				$("#processContent" + index).html("");
				$('<img src="' + imgdata + '" height="100">').appendTo($("#processContent3"));
				$('#myModal5').modal("hide");
				MessageBox.processClose();
			},
			error:function(XMLHttpRequest, data) {
				MessageBox.processEnd("系统错误，详细信息：" + data);
			}
		});*/
	};
};//end