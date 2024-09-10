 @Test
    public void testValidate_OperatingSystemAndroid_Disabled() throws ActionException {
        // Mock input ayarları: Android işletim sistemi
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (Android için 'Y' dönmeli)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("Y");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (Android OS staging disable edilmiş olmalı, true dönmeli)
        assertTrue(result);
    }
