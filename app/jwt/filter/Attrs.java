package jwt.filter;

import jwt.VerifiedJwt;
import models.*;
import play.libs.typedmap.TypedKey;

public class Attrs {
    public static final TypedKey<VerifiedJwt> VERIFIED_JWT = TypedKey.<VerifiedJwt>create("verifiedJwt");
    public static final TypedKey<Role> ROLE = TypedKey.<Role>create("role");
    public static final TypedKey<DoctorUser> VERIFIED_DOCTOR_USER = TypedKey.<DoctorUser>create("doctorUser");
    public static final TypedKey<NurseUser> VERIFIED_NURSE_USER = TypedKey.<NurseUser>create("nurseUser");
    public static final TypedKey<PatientUser> VERIFIED_PATIENT_USER = TypedKey.<PatientUser>create("patientUser");
    public static final TypedKey<RelativeUser> VERIFIED_RELATIVE_USER = TypedKey.<RelativeUser>create("relativeUser");
    public static final TypedKey<AdminUser> VERIFIED_ADMIN_USER = TypedKey.<AdminUser>create("adminUser");
}