<html>
    <head>
		<link rel="shortcut icon" href="${assetPath(src: 'dog.png')}" type="image/x-icon">
		<link rel="stylesheet" href="${request.contextPath}/purpose/css/bootstrap.min.css">
		<link rel="stylesheet" href="${request.contextPath}/purpose/css/icomoon-social.css">
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700,600,800' rel='stylesheet' type='text/css'>

		<link rel="stylesheet" href="${request.contextPath}/purpose/css/leaflet.css" />
		<!--[if lte IE 8]>
			<link rel="stylesheet" href="${request.contextPath}/purpose/css/leaflet.ie.css" />
		<![endif]-->
		<link rel="stylesheet" href="${request.contextPath}/purpose/css/main-orange.css">
		<script src="${request.contextPath}/purpose/js/modernizr-2.6.2-respond-1.1.0.min.js"></script>

		<link href="${request.contextPath}/purpose/calendar/fullcalendar.css" rel='stylesheet' />
		<link href="${request.contextPath}/purpose/calendar/fullcalendar.print.css" rel='stylesheet' media='print' />
		<script src="${request.contextPath}/purpose/calendar/jquery/jquery-1.10.2.js"></script>
		<script src="${request.contextPath}/purpose/calendar/jquery/jquery-ui.custom.min.js"></script>
		<script src="${request.contextPath}/purpose/calendar/fullcalendar.js"></script>

		<script >
			$(document).ready(function() {
	    		var date = new Date();
				var d = date.getDate();
				var m = date.getMonth();
				var y = date.getFullYear();

				/*  className colors
				className: default(transparent), important(red), chill(pink), success(green), info(blue)
				*/
				/* initialize the external events
				-----------------------------------------------------------------*/

				$('#external-events div.external-event').each(function() {
					// create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
					// it doesn't need to have a start or end
					var eventObject = {
						title: $.trim($(this).text()) // use the element's text as the event title
					};

					// store the Event Object in the DOM element so we can get to it later
					$(this).data('eventObject', eventObject);

					// make the event draggable using jQuery UI
					$(this).draggable({
						zIndex: 999,
						revert: true,      // will cause the event to go back to its
						revertDuration: 0  //  original position after the drag
					});
				});

				/* initialize the calendar
				-----------------------------------------------------------------*/
				var calendar =  $('#calendar').fullCalendar({
					header: {
						left: 'title',
						center: 'agendaDay,agendaWeek,month',
						right: 'prev,next today'
					},
					editable: true,
					firstDay: 1, //  1(Monday) this can be changed to 0(Sunday) for the USA system
					selectable: true,
					defaultView: 'month',
					axisFormat: 'h:mm',
					columnFormat: {
    		            month: 'ddd',    // Mon
        		        week: 'ddd d', // Mon 7
            		    day: 'dddd M/d',  // Monday 9/7
                		agendaDay: 'dddd d'
		            },
    		        titleFormat: {
        		        month: 'MMMM yyyy', // September 2009
            		    week: "MMMM yyyy", // September 2009
                		day: 'MMMM yyyy'                  // Tuesday, Sep 8, 2009
		            },
					allDaySlot: false,
					selectHelper: true,
					select: function(start, end, allDay) {
						var title = prompt('Event Title:');
						if (title) {
							calendar.fullCalendar('renderEvent',
								{
									title: title,
									start: start,
									end: end,
									allDay: allDay
								},
								true // make the event "stick"
							);
						}
						calendar.fullCalendar('unselect');
					},
					droppable: true, // this allows things to be dropped onto the calendar !!!
					drop: function(date, allDay) { // this function is called when something is dropped

						// retrieve the dropped element's stored Event Object
						var originalEventObject = $(this).data('eventObject');

						// we need to copy it, so that multiple events don't have a reference to the same object
						var copiedEventObject = $.extend({}, originalEventObject);

						// assign it the date that was reported
						copiedEventObject.start = date;
						copiedEventObject.allDay = allDay;

						// render the event on the calendar
						// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
						$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

						// is the "remove after drop" checkbox checked?
						if ($('#drop-remove').is(':checked')) {
							// if so, remove the element from the "Draggable Events" list
							$(this).remove();
						}
					},

					events: [
                        <g:each in="${activities_list}" var="aList">
    						{
	    						id: 999,
		    					title: "${aList.userOfService} ${aList.addressUbicationWorkArea}",
			    				start: new Date(<g:formatDate format="yyyy" date="${aList.dateUbicationWorkArea}"/>, <g:formatDate format="MM" date="${aList.dateUbicationWorkArea}"/>-1, <g:formatDate format="dd" date="${aList.dateUbicationWorkArea}"/>, 12, 0),
                                end: new Date(<g:formatDate format="yyyy" date="${aList.dateFinishReturnUnitK9}"/>, <g:formatDate format="MM" date="${aList.dateFinishReturnUnitK9}"/>-1, <g:formatDate format="dd" date="${aList.dateFinishReturnUnitK9}"/>, 12, 0),
                                allDay: false,
					    		className: 'info'
    						},
                        </g:each>
						{
							title: '.',
							start: new Date(y, m, d),
						}
					],
				});
			});
		</script>

		<style>
			body {
				margin-top: 40px;
				text-align: center;
				font-size: 14px;
				font-family: "Helvetica Nueue",Arial,Verdana,sans-serif;
				background-color: #DDDDDD;
			}

			#wrap {
				width: 1100px;
				margin: 0 auto;
			}

			#external-events {
				float: left;
				width: 150px;
				padding: 0 10px;
				text-align: left;
			}

			#external-events h4 {
				font-size: 16px;
				margin-top: 0;
				padding-top: 1em;
			}

			.external-event { /* try to mimick the look of a real event */
				margin: 10px 0;
				padding: 2px 4px;
				background: #3366CC;
				color: #fff;
				font-size: .85em;
				cursor: pointer;
			}

			#external-events p {
				margin: 1.5em 0;
				font-size: 11px;
				color: #666;
			}

			#external-events p input {
				margin: 0;
				vertical-align: middle;
			}

			#calendar {
		/* 		float: right; */
		        margin: 0 auto;
				width: 900px;
				background-color: #FFFFFF;
				  border-radius: 6px;
		        box-shadow: 0 1px 2px #C3C3C3;
			}
		</style>
        <title>Seguridog - Manejador</title>
    </head>

    <body>
        <div id='wrap'>
            <div id='calendar'></div>
            <div style='clear:both'></div>
        </div>
       <div class="section">
	    	<div class="container">
				<div class="row">
					<div class="col-sm-5 col-sm-offset-3">
						<div class="basic-login">
							<g:form controller = "K9Handler" action="index">
								<div class="form-group">
                                    <p align="center"><button type="submit" class="btn-orange">Inicio</button></p>
									<div class="clearfix"></div>
								</div>
							</g:form>
						</div>
					</div>
				</div>
			</div>
		</div>

    </body>
</html>