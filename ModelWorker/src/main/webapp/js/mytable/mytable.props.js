(function($){
	var myprops = {};
	var myeditor = $.myeditor;
	$.extend(true, myprops, {
			label:{
				value:{label:'显示名称', value:'', editor:function(){return new myeditor.label();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}}
			},
			inputEditor:{
				value:{label:'默认值',value:'',editor:function(){return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				readonly:{label:'是否只读',value:'N',editor:function() {return new myeditor.checkEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				validate:{label:'数据验证',value:'',editor:function() {return new myeditor.inputEditor();}},
				hidden:{label:'隐藏项目',value:'',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			},
			selectEditor:{
				value:{label:'默认值', value:'',editor:function(){return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function(){return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				readonly:{label:'是否只读',value:'N',editor:function() {return new myeditor.checkEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				validate:{label:'数据验证',value:'',editor:function(){return new myeditor.inputEditor();}},
				sql:{label:'数据取得SQL',value:'',editor:function(){return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			},
			openEditor:{
				value:{label:'默认值', value:'', editor:function() {return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				noField:{label:'编码字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				nameField:{label:'名称字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				openWinNo:{label:'开窗编码',value:'N',editor:function() {return new myeditor.inputEditor();}},
				mutilSelect:{label:'开窗画面是否多选', value:'N', editor:function() {return new myeditor.checkEditor();}},
				viewOk:{label:'开窗确定按钮处理', value:'', editor:function() {return new myeditor.openEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function() {return new myeditor.eventEditor();}}
			},
			checkEditor:{
				value:{label:'默认值', value:'N', editor:function() {return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				readonly:{label:'是否只读',value:'N',editor:function() {return new myeditor.checkEditor();}},
				viewText:{label:'显示文本', value:'', editor:function(){return new myeditor.inputEditor}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			},
			mutilInputEditor:{
				value:{label:'默认值', value:'', editor:function() {return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				readonly:{label:'是否只读',value:'N',editor:function() {return new myeditor.checkEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				validate:{label:'数据验证',value:'',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			},
			dateEditor:{
				value:{label:'默认值', value:'', editor:function() {return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				save:{label:'是否保存',value:'N',editor:function() {return new myeditor.checkEditor();}},
				readonly:{label:'是否只读',value:'N',editor:function() {return new myeditor.checkEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			},
			image:{
				value:{label:'默认值', value:'', editor:function() {return new myeditor.inputEditor();}},
				controlProp:{label:'控件属性', value:'', editor:function(){return new myeditor.inputEditor();}},
				field:{label:'字段名', value:'', editor:function() {return new myeditor.inputEditor();}},
				css:{label:'CSS样式',value:'N',editor:function() {return new myeditor.inputEditor();}},
				eventFunc:{label:'事件处理',value:'',editor:function(){return new myeditor.eventEditor();}},
				serverFunc:{label:'后台处理',value:'',editor:function(){return new myeditor.serverEditor();}}
			}
		}
	);
	$.myprops = myprops;
})(jQuery);