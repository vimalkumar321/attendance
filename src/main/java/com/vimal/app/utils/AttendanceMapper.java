package com.vimal.app.utils;

import com.vimal.app.entity.AttendanceLog;
import com.vimal.app.response.AttendanceResponse;

public class AttendanceMapper {
	public static AttendanceResponse attendanceToDto(AttendanceLog attendance) {
		return new AttendanceResponse(
				attendance.getId(),
				attendance.getWorker().getName(),
				attendance.getSite().getName(),
				attendance.getClockIn(),
				attendance.getClockOut(),
				attendance.getTotalHoursWorked(),
				attendance.getOvertimeHours());
	}
	
}
