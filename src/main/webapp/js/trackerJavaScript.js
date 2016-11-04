/*
	This project uses the Open Source jQuery plug-in DataTables. For license information view readme.md
*/
$(document).ready(function(){

	//Setting up the data table
	var dataTable = $("#carListTable").DataTable();
	var carTaskTable = $("#carMaintenanceTable").DataTable();

	//Setting up editCar information
	var currentCarInfo ={
		ID:"",
		type:"",
		make:"",
		model:"",
		year:"",
		odometer:""
	};

	//Setting up edit task information
	var currentTaskInfo ={
		ID:"",
		type:"",
		startDate:"",
		endDate:""
	};

	//Function that checks if there was any change between the given fields and the current car that is selected
	function checkCarInfoChange(ID, type, make, model, year, odometer){
		if(currentCarInfo.ID != ID || currentCarInfo.type != type || currentCarInfo.make != make ||
			 currentCarInfo.model != model || currentCarInfo.year != year || currentCarInfo.odometer != odometer){
			return true;
		}

		return false;
	}

	//Function that checks if there was any change between the given fields and the current task that is selected
	function checkTaskInfoChange(ID, type, startDate, endDate){
		if(currentTaskInfo.ID != ID || currentTaskInfo.type != type || currentTaskInfo.startDate != startDate || currentTaskInfo.endDate != endDate){
			return true;
		}
		return false;
	}
	//Clear forms on modal close
	$('#addCarModal').on('hidden.bs.modal', function () {
	$("#addCarAlert").hide();
	$(this).find("input").val('').end();
	});


	$('#addTaskModal').on('hidden.bs.modal', function () {
	$("#addTaskAlert").hide();
	$(this).find("input").val('').end();
	});

	//Show an alert in the car add modal
	function showAlert(text){
		$("#addCarAlert").html(text);
		$("#addCarAlert").show();
	}
	
	//Show an alert in the task add modal
	function showTaskAlert(text){
		$("#addTaskAlert").html(text);
		$("#addTaskAlert").show();
	}

	//Show an error alert on the main page
	function showMainPageErrorAlert(text){
		$("#mainPageError").html(text);
		$("#mainPageError").fadeIn();
		$("#mainPageError").delay(3000).fadeOut();
	}

	//Show an success alert on the main page
	function showMainPageSuccessAlert(text){
		$("#mainPageSuccess").html(text);
		$("#mainPageSuccess").fadeIn();
		$("#mainPageSuccess").delay(3000).fadeOut();
	}

	//Test if an string is a positive integer
	function isPostiveNumber(string) {
	return /^(0|[1-9]\d*)$/.test(string);
	}
	
	//Form validation of the car modal inputs
	function validateCar(ID,type,make,model,odometer,year){
		if(isNaN(ID) || !isPostiveNumber(ID)){
			showAlert("ID is not a valid number");
			return false;
		}else if(isNaN(odometer) || !isPostiveNumber(odometer)){
			showAlert("Odometer is not a valid number");
			return false;
		}else if(isNaN(year) || !isPostiveNumber(year)){
			showAlert("Year is not a valid number");
			return false;
		}else if(make.trim() == ""){
			showAlert("Make is not valid");
			return false;
		}else if(model.trim() == ""){
			showAlert("Model is not valid");
			return false;
		}

		return true;
	}
	
	//Form validation of the car update inputs
	function validateCarUpdate(ID,make,model,odometer,year){
		if(isNaN(ID) || !isPostiveNumber(ID)){
			showMainPageErrorAlert("ID is not a valid number");
			return false;
		}else if(isNaN(odometer) || !isPostiveNumber(odometer)){
			showMainPageErrorAlert("Odometer is not a valid number");
			return false;
		}else if(isNaN(year) || !isPostiveNumber(year)){
			showMainPageErrorAlert("Year is not a valid number");
			return false;
		}else if(make.trim() == ""){
			showMainPageErrorAlert("Make is not valid");
			return false;
		}else if(model.trim() == ""){
			showMainPageErrorAlert("Model is not valid");
			return false;
		}

		return true;
	}

	//Form validation for adding a task
	function validateTask(ID,type,startDate,endDate){
		if(isNaN(ID) || !isPostiveNumber(ID)){
			showTaskAlert("ID is not a valid number");
			return false;
		}

		return true;
	}
	
	//Form validation for updating a task
	function validateTaskUpdate(ID,type,startDate,endDate){
		if(isNaN(ID) || !isPostiveNumber(ID)){
			showMainPageErrorAlert("ID is not a valid number");
			return false;
		}

		return true;
	}
	//Fill out table with data already in the server on startup
	$.ajax({
    type: "POST",
    url: "ListServlet",
    data: {"requestType": "listCars"},
    dataType: "json",
    success: function( data, textStatus, jqXHR){
    	for(var i = 0; i<data.cars.length; i++){
    		var currentRow = data.cars[i];
    		dataTable.row.add([currentRow.carID, currentRow.type, currentRow.make, currentRow.model, currentRow.year, currentRow.odometer]).draw(false);
    	}
    },error: function(jqXHR, textStatus, errorThrown){
    	alert("Error accessing database");
    }
	});

	//Add an car from the database to the datatable on the site
	function addCarToTable(carID){
		$.ajax({
    type: "POST",
    url: "ListServlet",
    data: {"requestType": "getCar", "carID": carID },
    dataType: "json",
    success: function( data, textStatus, jqXHR){
   		if(data.status == "success"){
   			currentRow = data.car;	
    		dataTable.row.add([currentRow.carID, currentRow.type, currentRow.make, currentRow.model, currentRow.year, currentRow.odometer]).draw(false);
    		showMainPageSuccessAlert("Added Car to Table");
    	}else{
    		alert("Car with ID:" + ID + " could not be added to table");
    	}
    },error: function(jqXHR, textStatus, errorThrown){
    	alert("Could not add car");
    }
		});
	}

	//Removing a car from the main dataTable 
	function removeCarFromTable(carID){
		$.ajax({
    type: "POST",
    url: "RemoveServlet",
    data: {"requestType": "removeCar", "carID": carID },
    dataType: "json",
    success: function( data, textStatus, jqXHR){
   		
   		//Remove the rows with carID from the table
   		if(data.status == "success"){
   			dataTable.row(function(idx, data, node){
									return data[0] ==  carID ? true:false;
							}).remove().draw();

   			showMainPageSuccessAlert("Successfully Removed Car");
    	}else{
    		mainPageError("Could not remove car");
    	}
    },error: function(jqXHR, textStatus, errorThrown){
    	mainPageError("Could not remove car");
    }
		});
	}

	//Removing a car from the main dataTable 
	function removeTaskFromTable(carID, taskID){
		$.ajax({
    type: "POST",
    url: "RemoveServlet",
    data: {"requestType": "removeTask", "carID": carID, "taskID": taskID },
    dataType: "json",
    success: function( data, textStatus, jqXHR){
   		
   		//Remove the rows with carID from the table
   		if(data.status == "success"){
   			carTaskTable.row(function(idx, data, node){
									return data[0] ==  taskID ? true:false;
							}).remove().draw();
   			showMainPageSuccessAlert("Successfully Removed Task");
    	}else{
    		mainPageError("Could not remove task");
    	}
    },error: function(jqXHR, textStatus, errorThrown){
    	mainPageError("Could not remove task");
    }
		});
	}

	//When the add car modal is submitted
	$("#AddCarForm").submit(function(e){
					e.preventDefault();
					//Getting values
					var ID = $("#addCarFormID").val();
					var type = $("#addCarFormType").val();
					var make = $("#addCarFormMake").val();
					var model = $("#addCarFormModel").val();
					var odometer = $("#addCarFormOdometer").val();
					var year = $("#addCarFormYear").val();

					//Form validation
					if(!validateCar(ID,type,make,model,odometer,year)){
						return;
					}

					//ajax call to servlet
					$.ajax({
		        type: "POST",
		        url: "AddCarServlet",
		        data: {"ID": ID, "type": type, "make": make, "model": model, "year":year, "odometer": odometer},
		        dataType: "json",
		        success: function( data, textStatus, jqXHR){
		        	
		        	if(data.status == "failed"){
		        		switch(data.reason){
		        			case "carID exists":
		        				showAlert("Car ID already registered");
		        				break;
		        				
		        			case "carID NaN":
		        				showAlert("ID is not a valid number");
		        				break; 
		        			case "year NaN":
		        				showAlert("Year is not a valid number");
		        				break;
		        			case "odometer NaN":
		        				showAlert("Odometer is not a valid number");
		        				break;
		        			default:
		        				showAlert("Invalid inputs");
		        		}
		        		
		        	}else if(data.status == "success"){
		        		$("#addCarModal").modal('toggle');
		        		addCarToTable(ID);
   						showMainPageSuccessAlert("Successfully Added Car");

		        	}else{
		        		showAlert("Error processing inputs");
		        	}
		        	
		        	
		        },
		        //error function
		        error: function(jqXHR, textStatus, errorThrown){
		        	showAlert("Error adding car");
		        }
		      	});

	});

	//Submitting the forms. Done with separate button clicks
	//in order to place buttons in footer 
	$("#AddCar").click(function(){
		$("#sub").click();
		
	});

	$("#addTask").click(function(){
		$("#taskSub").click();
	});

	//Function that will set the current car information that is showing
	function setCurrentCarInfo(){
		currentCarInfo.ID = $("#carInfoID").val();
		currentCarInfo.type = $("#carInfoType").val();
		currentCarInfo.make = $("#carInfoMake").val();
		currentCarInfo.model = $("#carInfoModel").val();
		currentCarInfo.year = $("#carInfoYear").val();
		currentCarInfo.odometer = $("#carInfoOdometer").val();
		
	}

	//function that will set the current task info
	function setCurrentTaskInfo(){
		currentTaskInfo.ID = $("#taskInfoID").val();
		currentTaskInfo.type = $("#taskInfoType").val();
		currentTaskInfo.startDate = $("#taskInfoStartDate").val();
		currentTaskInfo.endDate = $("#taskInfoEndDate").val();
	}

	//Function that will disable car info fields
	function disableCarInfoFields(){
			$("#carInfoID").attr("readonly", true);
			$("#carInfoID").addClass("input-disabled");
			$("#carInfoType").attr("disabled", "disabled");
			$("#carInfoType").addClass("input-disabled");
			$("#carInfoMake").attr("readonly", true);
			$("#carInfoMake").addClass("input-disabled");
			$("#carInfoModel").attr("readonly", true);
			$("#carInfoModel").addClass("input-disabled");
			$("#carInfoYear").attr("readonly", true);
			$("#carInfoYear").addClass("input-disabled");
			$("#carInfoOdometer").attr("readonly", true);
			$("#carInfoOdometer").addClass("input-disabled");
			$("#saveCarInfoButton").hide();
			$('#cancelCarInfoButton').hide();

	}

	//Function that will disable task info fields
	function disableTaskInfoFields(){
			$("#taskInfoID").attr("readonly", true);
			$("#taskInfoID").addClass("input-disabled");
			$("#taskInfoType").attr("disabled", "disabled");
			$("#taskInfoType").addClass("input-disabled");
			$("#taskInfoStartDate").attr("readonly", true);
			$("#taskInfoStartDate").addClass("input-disabled");
			$("#taskInfoEndDate").attr("readonly", true);
			$("#taskInfoEndDate").addClass("input-disabled");
			$("#saveTaskInfoButton").hide();
			$('#cancelTaskInfoButton').hide();

	}

	//Handle car list table clicks and fill car information
	$('#carListTable tbody').on('click', 'tr', function () {
	var data = dataTable.row( this ).data();
	
	//Highlight the selected row
	dataTable.$('tr.selected').removeClass('selected');
	$(this).addClass('selected');
$("#carInformation").fadeOut(0.1);
$("#taskInformation").hide();

//Get values to fill information fields
$("#carInfoID").val(data[0]);
$("#carInfoType").val(data[1]);
$("#carInfoMake").val(data[2]);
$("#carInfoModel").val(data[3]);
$("#carInfoYear").val(data[4]);
$("#carInfoOdometer").val(data[5]);
$("#carInformation").fadeIn();

//Set the current car information
currentCarInfo.ID = data[0];
		currentCarInfo.type = data[1];
		currentCarInfo.make =data[2];
		currentCarInfo.model = data[3];
		currentCarInfo.year = data[4];
		currentCarInfo.odometer = data[5];

		//Clearing the car maintenance task tables
		carTaskTable.clear().draw();
		
		//Fill out the cars maintenance task table
		$.ajax({
    type: "POST",
    url: "ListServlet",
    data: {"requestType": "getCarTasks", "carID": currentCarInfo.ID},
    dataType: "json",
    success: function( data, textStatus, jqXHR){
    	
    	for(var i = 0; i<data.tasks.length; i++){
    		var currentRow = data.tasks[i];
    		carTaskTable.row.add([currentRow.taskID, currentRow.type, currentRow.startDate, currentRow.endDate]).draw(false);
    	}
    },error: function(jqXHR, textStatus, errorThrown){
    	alert("Error accessing database");
    }
		});

});

	//Enable editing of car info when the car edit button is clicked
	$("#editCarButton").click(function(){

			$("#editCarButton").html("Edit Car Information");
			$("#carInfoID").attr("readonly", false);
			$("#carInfoID").removeClass("input-disabled");
			$("#carInfoType").removeAttr("disabled");
			$("#carInfoType").removeClass("input-disabled");
			$("#carInfoMake").attr("readonly", false);
			$("#carInfoMake").removeClass("input-disabled");
			$("#carInfoModel").attr("readonly", false);
			$("#carInfoModel").removeClass("input-disabled");
			$("#carInfoYear").attr("readonly", false);
			$("#carInfoYear").removeClass("input-disabled");
			$("#carInfoOdometer").attr("readonly", false);
			$("#carInfoOdometer").removeClass("input-disabled");
			$("#saveCarInfoButton").show();
			$("#cancelCarInfoButton").show();
			setCurrentCarInfo();
		
	});

	//Reset the car info on cancel editing button click
	$("#cancelCarInfoButton").click(function(){

			//Restore defaults
			$("#carInfoID").val(currentCarInfo.ID);
    $("#carInfoType").val(currentCarInfo.type);
    $("#carInfoMake").val(currentCarInfo.make);
    $("#carInfoModel").val(currentCarInfo.model);
    $("#carInfoYear").val(currentCarInfo.year);
    $("#carInfoOdometer").val(currentCarInfo.odometer);
			
			disableCarInfoFields();
			
	});

	//Saving the updated car info
	$("#saveCarInfoButton").click(function(){
		
		var data = $("#carInfoForm").serializeArray();
		var oldCarID = currentCarInfo.ID;
		var newCarID = data[0].value;
		var type = data[1].value;
		var make = data[2].value;
		var model = data[3].value;
		var year = data[4].value;
		var odometer = data[5].value;
		
		//Form validation
		if(!validateCarUpdate(newCarID, make, model, odometer, year)){
			return;
		}

		//Check if any of the fields were changed before making a server request
		if(!checkCarInfoChange(newCarID, type, make, model, year, odometer)){
			showMainPageErrorAlert("No information was changed");
			return;
		}

		//ajax call to update the car with new information
		$.ajax({
		        type: "POST",
		        url: "UpdateCarServlet",
		        data: {"ID": oldCarID, "newID": newCarID, "type": type, "make": make, "model": model, "year":year, "odometer": odometer},
		        dataType: "json",
		        success: function( data, textStatus, jqXHR){
		        	
		        	if(data.status == "failed"){
		        		switch(data.reason){
		        			case "carID exists":
		        				showMainPageErrorAlert("Car ID already registered");
		        				break;
		        				
		        			case "carID NaN":
		        				showMainPageErrorAlert("ID is not a valid number");
		        				break; 
		        			case "year NaN":
		        				showMainPageErrorAlert("Year is not a valid number");
		        				break;
		        			case "odometer NaN":
		        				showMainPageErrorAlert("Odometer is not a valid number");
		        				break;
		        			case "carID not in system":
		        				showMainPageErrorAlert("Old Car ID not in system");
		        				break;
		        			case "carID exists":
		        				showMainPageErrorAlert("New Car ID already in system");
		        				break;
		        			case "Invalid type change":
		        				showMainPageErrorAlert("Invalid type change. Car contains maintenance tasks preventing type change.");
		        				break;
		        			default:
		        				showMainPageErrorAlert("Invalid inputs");
		        		}
		        		
		        	}else if(data.status == "success"){
		        		
		        		//Updating the row locally
		        		dataTable.row(function(idx, data, node){
									return data[0] ==  oldCarID ? true:false;
								}).data([newCarID, type, make, model, year, odometer]);

								dataTable.draw();
								disableCarInfoFields();
								setCurrentCarInfo();
   						showMainPageSuccessAlert("Successfully Updated Car");
		        	}else{
		        		showMainPageErrorAlert("Error processing inputs");
		        	}
		        	
		        	
		        },
		        error: function(jqXHR, textStatus, errorThrown){
		        	showMainPageErrorAlert("Error updating car");
		        }
		});
		
		
	});

	//Remove the current car that is selected on remove car button click
	$("#removeCarInfoButton").click(function(){
		removeCarFromTable(currentCarInfo.ID);
		disableCarInfoFields();
		$("#carInformation").hide();
	});

	//Launching the add task model with the correct task type select fields based on the current car type
	$("#mainAddTask").click(function(){
		var type = currentCarInfo.type;
		if(type == "Gas Car" || type == "Diesel Car"){
			$("#addTaskFormType").html("<option value='Tire Rotations'>Tire Rotations</option> <option value='Oil Change'>Oil Change</option>")

		}else if (type == "Electric Car"){
			$("#addTaskFormType").html("<option value='Tire Rotations'>Tire Rotations</option> <option value='Electric Battery Replacement'>Electric Battery Replacement</option>")
		}
	});

	//Adding a task form the add task modal 
	$("#addTaskForm").submit(function(e){
					
					e.preventDefault();
					var carID = currentCarInfo.ID;
					var taskID = $("#addTaskFormID").val();
					var type = $("#addTaskFormType").val();
					var startDate = $("#addTaskFormSDate").val();
					var endDate = $("#addTaskFormEDate").val();

					//Form validation
					if(!validateTask(taskID,type,startDate, endDate)){
						return;
					}

					//Ajax call to add the task to server
					$.ajax({
		        type: "POST",
		        url: "AddTaskServlet",
		        data: {"carID": carID, "taskID": taskID, "type": type, "startDate": startDate, "endDate": endDate},
		        dataType: "json",
		        success: function( data, textStatus, jqXHR){
		        	
		        	if(data.status == "failed"){
		        		switch(data.reason){
		        			case "carID does not exist":
		        				showTaskAlert("Car ID not in system");
		        				break;
		        			case "taskID exists":
		        				showTaskAlert("Task ID already registered");
		        				break;

		        			case "carID NaN":
		        				showTaskAlert("Car ID is not a valid number");
		        				break; 
		        			case "taskID NaN":
		        				showTaskAlert("TaskID is not a valid number");
		        				break;
		        			default:
		        				showTaskAlert("Invalid inputs");
		        		}
		        		
		        	}else if(data.status == "success"){
		        		$("#addTaskModal").modal('toggle');
		        		carTaskTable.row.add([taskID, type, startDate, endDate]).draw(false);
   						showMainPageSuccessAlert("Successfully Added Task");
		        	}else{
		        		showAlert("Error processing inputs");
		        	}
		        	
		        	
		        },
		        error: function(jqXHR, textStatus, errorThrown){
		        	showAlert("Error adding task");
		        }
		      	});

	});


	//Handle maintenance task list clicks and fill information
	$('#carMaintenanceTable tbody').on('click', 'tr', function () {
	var data = carTaskTable.row( this ).data();
	carTaskTable.$('tr.selected').removeClass('selected');
	$(this).addClass('selected');
$("#taskInformation").fadeOut(0.1);


$("#taskInfoID").val(data[0]);
$("#taskInfoType").val(data[1]);
$("#taskInfoStartDate").val(data[2]);
$("#taskInfoEndDate").val(data[3]);
$("#taskInformation").fadeIn();

currentTaskInfo.ID = data[0];
		currentTaskInfo.type = data[1];
		currentTaskInfo.startDate =data[2];
		currentTaskInfo.endDate = data[3];

});

//Enable editing of car info when the car edit button is clicked
	$("#editTaskButton").click(function(){

			
			$("#taskInfoID").attr("readonly", false);
			$("#taskInfoID").removeClass("input-disabled");
			$("#taskInfoType").removeAttr("disabled");
			$("#taskInfoType").removeClass("input-disabled");
			$("#taskInfoStartDate").attr("readonly", false);
			$("#taskInfoStartDate").removeClass("input-disabled");
			$("#taskInfoEndDate").attr("readonly", false);
			$("#taskInfoEndDate").removeClass("input-disabled");
			$("#saveTaskInfoButton").show();
			$("#cancelTaskInfoButton").show();
			setCurrentTaskInfo();
		
	});

	//Cancel editing task information by reseting and disabling fields
	$("#cancelTaskInfoButton").click(function(){
		$("#taskInfoID").val(currentTaskInfo.ID);
$("#taskInfoType").val(currentTaskInfo.type);
$("#taskInfoStartDate").val(currentTaskInfo.startDate);
$("#taskInfoEndDate").val(currentTaskInfo.endDate);

disableTaskInfoFields();
	});


	//Submitting the save task form
	$("#saveTaskInfoButton").click(function(){
		$("#subEditTaskInfo").click();
	});

	//Updating task information on form submit
	$("#carInfoTasks").submit(function(e){
		e.preventDefault();
		
		//Getting form data
		var data = $("#carInfoTasks").serializeArray();
		var carID = currentCarInfo.ID;
		var oldTaskID = currentTaskInfo.ID;
		var newTaskID = data[0].value;
		var type = data[1].value;
		var startDate = data[2].value;
		var endDate = data[3].value;
		
		//form validation
		if(!validateTaskUpdate(newTaskID, type, startDate, endDate)){
			return;
		}

		//Check if any of the fields were changed before making a server request
		if(!checkTaskInfoChange(newTaskID,type, startDate, endDate)){
			showMainPageErrorAlert("No information was changed");
			return;
		}
		
		//Ajax call to update the task in the servlet
		$.ajax({
		        type: "POST",
		        url: "UpdateTaskServlet",
		        data: {"carID" : carID, "ID": oldTaskID, "newID": newTaskID, "type": type, "startDate": startDate, "endDate": endDate},
		        dataType: "json",
		        success: function( data, textStatus, jqXHR){
		        	
		        	if(data.status == "failed"){
		        		switch(data.reason){
		        			case "carID NaN":
		        				showMainPageErrorAlert("Car ID not a number");
		        				break;
		        				
		        			case "taskID NaN":
		        				showMainPageErrorAlert("task ID is not a valid number");
		        				break;

		        			case "new taskID NaN":
		        				showMainPageErrorAlert("New task ID is not a valid number");
		        				break; 
		 				    	case "taskID not in system":
		        				showMainPageErrorAlert("Old Task ID not in system");
		        				break;
		        			case "new taskID exists":
		        				showMainPageErrorAlert("New Task ID already in system");
		        				break;
		        			case "Invalid type change":
		        				showMainPageErrorAlert("Invalid type change. Car is not valid type for task type swap.");
		        				break;
		        			default:
		        				showMainPageErrorAlert("Invalid inputs");
		        		}
		        		
		        	}else if(data.status == "success"){
		        		
		        		//Updating the row locally
		        		carTaskTable.row(function(idx, data, node){
									return data[0] ==  oldTaskID ? true:false;
								}).data([newTaskID, type, startDate, endDate]);

								carTaskTable.draw();
								disableTaskInfoFields();
								setCurrentTaskInfo();
   						showMainPageSuccessAlert("Successfully Updated Task");
		        	}else{
		        		showMainPageErrorAlert("Error processing inputs");
		        	}
		        	
		        	
		        },
		        error: function(jqXHR, textStatus, errorThrown){
		        	showMainPageErrorAlert("Error updating task");
		        }
		});
	});

	//Remove the current task that is selected on remove task button click
	$("#removeTaskInfoButton").click(function(){
		removeTaskFromTable(currentCarInfo.ID, currentTaskInfo.ID);
		disableTaskInfoFields();
		$("#taskInformation").hide();
	});

});/*End of document ready*/