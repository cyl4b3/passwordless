package org.openidentityplatform.passwordless.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.StringUtils;
import org.openidentityplatform.passwordless.models.OTPSetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class FileBasedOTPSettingsRepository implements OTPSettingsRepository {


    @Value("${otp.settings.config:classpath:otp-settings.yaml}")
    private String configPath;

    private List<OTPSetting> otpSettings;

    @PostConstruct
    private void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            otpSettings = objectMapper.readValue(ResourceUtils.getFile(configPath), new TypeReference<List<OTPSetting>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OTPSetting getSetting(String settingId) {
        return otpSettings.stream()
                .filter(o -> StringUtils.equals(settingId, o.getId()))
                .findFirst().orElse(null);

    }
}