    @InjectMocks
    private OperatingSystemStagingPolicy operatingSystemStagingPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Mock
    private ArkParameterServiceOutput mockServiceOutput;

    @Before
    public void setUp() {
        operatingSystemStagingPolicy = new OperatingSystemStagingPolicy(mockDynamicService);
    }

    @Test
    public void testValidate_Android_Success() throws ActionException {
        // Android işletim sistemi ve ilgili parametreleri doğru ayarlıyoruz
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getIosFeatureControlParameter()).thenReturn("");  // iOS boş olabilir
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (Android için 'Y' dönecek)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("Y");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Android OS staging disable edilmiş olmalı, true döner
        assertTrue(result);
    }

    @Test
    public void testValidate_IOS_Success() throws ActionException {
        // iOS işletim sistemi ve ilgili parametreleri doğru ayarlıyoruz
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.IOS);
        when(mockInput.getIosFeatureControlParameter()).thenReturn("iosFeatureControlParam");
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("");  // Android boş olabilir
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (iOS için 'Y' dönecek)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("Y");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // iOS OS staging disable edilmiş olmalı, true döner
        assertTrue(result);
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsExceptionForInvalidInput() throws ActionException {
        // Hem Android hem de iOS parametreleri boş olduğunda istisna fırlatılmalı
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("");
        when(mockInput.getIosFeatureControlParameter()).thenReturn("");

        operatingSystemStagingPolicy.validate(mockInput);
    }

    @Test
    public void testValidate_OperatingSystemNotInStagingList() throws ActionException {
        // Android işletim sistemi ve ilgili parametreler
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getIosFeatureControlParameter()).thenReturn("");  // iOS boş olabilir
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (Android için 'N' dönecek, staging listede değil)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("N");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Android OS staging listede değil, false döner
        assertFalse(result);
    }
}
