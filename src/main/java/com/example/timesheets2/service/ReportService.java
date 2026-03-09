package com.example.timesheets2.service;

import com.example.timesheets2.StringUtils;
import com.example.timesheets2.domain.User;
import com.example.timesheets2.exception.UserNotFoundException;
import com.example.timesheets2.pdf.StyledTable;
import com.example.timesheets2.persistance.entity.ProjectUser;
import com.example.timesheets2.persistance.entity.WorkRecord;
import org.openpdf.text.Document;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import java.awt.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final UserService userService;
    private final WorkRecordService workRecordService;
    private final ProjectService projectService;
    private final ProjectUserService projectUserService;
    private final NumberFormat numberFormatter;

    ReportService(UserService userService, WorkRecordService workRecordService, ProjectService projectService, ProjectUserService projectUserService, @Value("${com.example.country}") String country) {
        this.userService = userService;
        this.workRecordService = workRecordService;
        this.projectService = projectService;
        this.projectUserService = projectUserService;
        var locale = new Locale.Builder().setRegion(country).build();
        this.numberFormatter = NumberFormat.getCurrencyInstance(locale);
    }

    static String toHoursAndMinutes(Integer minutes) {
        return String.format("%d h %d min", minutes / 60, minutes % 60);
    }

    public void getReport(LocalDate start, LocalDate end, Long projectId, List<String> uids, OutputStream outputStream) throws NamingException {
        var userIds = uids.stream().filter(uid -> !StringUtils.isBlank(uid)).toList();
        var projectUsers = projectUserService.getProjectUsers(projectId);
        var projectUsersIds = projectUsers.stream().map(ProjectUser::getUserId).collect(Collectors.toSet());
        var missingUsers = userIds.stream().filter(id -> !projectUsersIds.contains(id)).toList();
        if (!missingUsers.isEmpty()) throw new UserNotFoundException(missingUsers);
        var users = userService.getUsers();
        var project = projectService.getProject(projectId);
        final var reportUsers = users.stream().filter(user -> userIds.contains(user.id())).toList();
        var userHourlyCosts = projectUsers.stream().collect(Collectors.toMap(ProjectUser::getUserId, ProjectUser::getHourlyCost));
        var userTimeTotals = reportUsers.stream().collect(Collectors.toMap(User::id, u -> 0));
        var records = workRecordService.getWorkRecordsBetweenDate(start, end, projectId, userIds);
        var recordsGroupedByDateAndUser = records.stream().collect(Collectors.groupingBy(WorkRecord::getDate, Collectors.groupingBy(WorkRecord::getUserId)));
        var dates = records.stream().map(WorkRecord::getDate).sorted().distinct().toList();
        try (var document = new Document(PageSize.A4)) {
            var instance = PdfWriter.getInstance(document, outputStream);
            instance.open();
            document.open();
            var paragraph = new Paragraph("Report", FontFactory.getFont(Font.SERIF, 24));
            paragraph.setSpacingAfter(4.0f);
            document.add(paragraph);
            paragraph = new Paragraph("Project: " + project.getName() + "\n Report's timeframe: " + start.toString() + " to " + end.toString());
            paragraph.setSpacingAfter(10.0f);
            document.add(paragraph);
            var size = userIds.size() + 1;
            float[] columnDefinitionSize = new float[size];
            columnDefinitionSize[0] = 15.0f;
            for (int i = 1; i < size; i++) {
                columnDefinitionSize[i] = 25.0f;
            }
            var table = new StyledTable(columnDefinitionSize);
            table.addCell("Date");
            reportUsers.forEach(user -> table.addCell(user.username()));
            table.endHeader();
            dates.forEach(date -> {
                table.addCell(date.toString());
                reportUsers.forEach(user -> {
                    var usersRecords = recordsGroupedByDateAndUser.get(date).get(user.id());
                    if (usersRecords != null) {
                        var totalTimeInMinutes = usersRecords.stream().map(WorkRecord::getMinutes).mapToInt(Integer::intValue).sum();
                        userTimeTotals.put(user.id(), userTimeTotals.get(user.id()) + totalTimeInMinutes);
                        table.addCell(new Phrase(toHoursAndMinutes(totalTimeInMinutes) + "\n" + String.join("\n", usersRecords.stream().map(WorkRecord::getDescription).filter(Objects::nonNull).toList())));
                    } else table.addCell(new Phrase());
                });
            });
            table.startFooter();
            table.addCell("Summary");
            reportUsers.forEach(user -> table.addCell(toHoursAndMinutes(userTimeTotals.get(user.id())) + "\n" + numberFormatter.format(userHourlyCosts.get(user.id()).multiply(BigDecimal.valueOf(userTimeTotals.get(user.id()))))));
            document.add(table.toPdfPTable());
        }
    }

}
