//package com.yuziak;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.sheets.v4.Sheets;
//import com.google.api.services.sheets.v4.SheetsScopes;
//import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
//import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
//import com.google.api.services.sheets.v4.model.ValueRange;
//
//import java.io.*;
//import java.security.GeneralSecurityException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class SheetApi {
//    private static final String APPLICATION_NAME = "RebelsDisBot";
//
//    private static final String CREDENTIALS_FILE_PATH_SERV_ACC = "/serviceAccount.json";
//
//    private static final String sheetName = "BotTest!";
//
//    private static final String spreadsheetId = "1iy1WQleEMfo_GRXpwrEzf_RtWcxFrvE-p0AKUU1IsRY";
//
//    private static Set<String> googleOAuth2Scopes() {
//        Set<String> googleOAuth2Scopes = new HashSet<>();
//        googleOAuth2Scopes.add(SheetsScopes.SPREADSHEETS);
//        return Collections.unmodifiableSet(googleOAuth2Scopes);
//    }
//
//    private static Sheets getSheets() throws IOException, GeneralSecurityException {
//        InputStream in = SheetApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH_SERV_ACC);
//        if (in != null) {
//            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleCredential.fromStream(in).createScoped(googleOAuth2Scopes()))
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//        }else throw new NullPointerException("File don`t exist");
//    }
//
//    public static int getUserPos(String name) throws IOException, GeneralSecurityException {
//        final String range = sheetName + "B2:K";
//
//        Sheets service = getSheets();
//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//
//        List<List<Object>> values = response.getValues();
//
//        int userPos = Integer.MAX_VALUE;
//        int tempPos = 2;
//        if (values == null || values.isEmpty()) {
//            return 0;
//        } else {
//            for (List row : values) {
//                if (row.get(0).toString().equals(name)) {
//                    userPos = tempPos;
//                } else {
//                    tempPos++;
//                }
//            }
//        }
//        if (userPos == Integer.MAX_VALUE) {
//            return 0;
//        } else {
//            return userPos;
//        }
//    }
//
//    public static String updateMember(String name, String gameClass, Integer ap, Integer def, Integer accuracy, Integer horseDef, String ckrockType, String horseType) throws IOException, GeneralSecurityException {
//        String pattern = "dd.MM.yyyy hh:mm:ss";
//        LocalDateTime curTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
//        String updateDate = curTime.format(DateTimeFormatter.ofPattern(pattern));
//
//        Sheets service = getSheets();
//        int userPos = getUserPos(name);
//
//        if (userPos == 0) {
//            return ("User not found");
//        } else {
//            List<ValueRange> data = new ArrayList<>();
//            data.add(new ValueRange().setRange(sheetName + "B" + userPos).setValues(Collections.singletonList(Collections.singletonList(name))));
//            data.add(new ValueRange().setRange(sheetName + "C" + userPos).setValues(Collections.singletonList(Collections.singletonList(gameClass))));
//            try {
//                if (ap <= 100 || ap > 330) {
//                    return "ApNull";
//                }
//            } catch (NullPointerException ignored) {
//            }
//            data.add(new ValueRange().setRange(sheetName + "D" + userPos).setValues(Collections.singletonList(Collections.singletonList(ap))));
//            try {
//                if (def <= 100 || def > 600) {
//                    return "DefNull";
//                }
//            } catch (NullPointerException ignored) {
//            }
//            data.add(new ValueRange().setRange(sheetName + "F" + userPos).setValues(Collections.singletonList(Collections.singletonList(def))));
//            data.add(new ValueRange().setRange(sheetName + "A" + userPos).setValues(Collections.singletonList(Collections.singletonList(updateDate))));
//            try {
//                if (horseDef <= 0 || horseDef > 250) {
//                    return "HorseDefNull";
//                }
//            } catch (NullPointerException ignored) {
//            }
//            data.add(new ValueRange().setRange(sheetName + "H" + userPos).setValues(Collections.singletonList(Collections.singletonList(horseDef))));
//            data.add(new ValueRange().setRange(sheetName + "I" + userPos).setValues(Collections.singletonList(Collections.singletonList(ckrockType))));
//            data.add(new ValueRange().setRange(sheetName + "J" + userPos).setValues(Collections.singletonList(Collections.singletonList(horseType))));
//            try {
//                if (accuracy <= 250 || accuracy > 500) {
//                    return "AccuracyNull";
//                }
//            } catch (NullPointerException ignored) {
//            }
//            data.add(new ValueRange().setRange(sheetName + "E" + userPos).setValues(Collections.singletonList(Collections.singletonList(accuracy))));
//            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
//                    .setValueInputOption("RAW")
//                    .setData(data);
//            BatchUpdateValuesResponse result =
//                    service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
//
//            System.out.println("User position "+userPos);
//            System.out.printf("%d cells updated.", result.getTotalUpdatedCells());
//
//            return ("Successful update");
//
//        }
//    }
//}