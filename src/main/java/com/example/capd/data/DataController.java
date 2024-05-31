package com.example.capd.data;

import com.example.capd.User.dto.CareerParam;
import com.example.capd.User.dto.ParticipationParam;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.UserDTO;
import com.example.capd.User.service.ParticipationService;
import com.example.capd.User.service.ProfileService;
import com.example.capd.User.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DataController {

    private final ObjectMapper objectMapper;
    private final ProfileService profileService;
    private final ParticipationService participationService;
    private final UserService userService;

    @GetMapping("/data")
    public String main() {

        return "main"; //메인화면
    }

    @GetMapping("/success")
    public String sucess() {
        return "success"; //성공화면
    }


    @PostMapping("/addUserExcel")
    public String readUserExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            UserDTO user = new UserDTO();
            user.setUserId(formatter.formatCellValue(row.getCell(0)));
            user.setUsername(formatter.formatCellValue(row.getCell(1)));
            user.setPassword(formatter.formatCellValue(row.getCell(2)));
            user.setAddress(formatter.formatCellValue(row.getCell(3)));
            user.setEmail(formatter.formatCellValue(row.getCell(4)));
            user.setPhone(formatter.formatCellValue(row.getCell(5)));
            user.setGender(formatter.formatCellValue(row.getCell(6)).charAt(0));
            user.setTendency(formatter.formatCellValue(row.getCell(7)));
            userService.save(user);
        }
        workbook.close();
        return "redirect:/success";
    }

    @PostMapping("/addProfileExcel")
    public String readProfileExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            List<CareerParam> careers = objectMapper.readValue(formatter.formatCellValue(row.getCell(6)), new TypeReference<List<CareerParam>>() {});
            List<String> stackList = objectMapper.readValue(formatter.formatCellValue(row.getCell(5)), new TypeReference<List<String>>() {});
            int myTime = Integer.parseInt(formatter.formatCellValue(row.getCell(4)));
            int desiredTime = Integer.parseInt(formatter.formatCellValue(row.getCell(3)));
            int collaborationCount = Integer.parseInt(formatter.formatCellValue(row.getCell(2)));

            ProfileRequestDto profile = ProfileRequestDto.builder()
                    .userId(formatter.formatCellValue(row.getCell(0)))
                    .intro(formatter.formatCellValue(row.getCell(1)))
                    .collaborationCount(collaborationCount)
                    .desiredTime(desiredTime)
                    .myTime(myTime)
                    .stackList(stackList)
                    .build();
            profile.setCareers(careers);
            profileService.saveProfile(profile);
        }
        workbook.close();
        return "redirect:/success";
    }

    @PostMapping("/addParticipationExcel")
    public String readParticipationExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            List<String> stackList = Arrays.asList(formatter.formatCellValue(row.getCell(3)).split(","));

            ParticipationParam participation = ParticipationParam.builder()
                    .contestId(Long.parseLong(formatter.formatCellValue(row.getCell(0))))
                    .userId(formatter.formatCellValue(row.getCell(1)))
                    .additional(formatter.formatCellValue(row.getCell(2)))
                    .stackList(stackList)
                    .build();

            participationService.saveParticipation(participation);
        }
        workbook.close();
        return "redirect:/success";
    }

}
