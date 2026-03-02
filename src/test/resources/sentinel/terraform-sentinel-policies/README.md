# Fixture Provenance

このディレクトリ配下の `.sentinel` ファイルは、以下の upstream から取得したテスト用 fixture です。

- Repository: `https://github.com/hashicorp/terraform-sentinel-policies`
- Upstream HEAD at import time: `b0bd9902f1db35f922f7d33ff10c35e88c380f0e`

## Imported Files

- `aws/restrict-ami-owners.sentinel`
- `aws/mocks/ec2-instance-mock-tfrun.sentinel`
- `cloud-agnostic/prohibited-providers.sentinel`
- `common-functions/report/report.sentinel`

## Refresh Procedure

```bash
git clone --depth 1 https://github.com/hashicorp/terraform-sentinel-policies /tmp/terraform-sentinel-policies
cp /tmp/terraform-sentinel-policies/aws/restrict-ami-owners.sentinel \
  src/test/resources/sentinel/terraform-sentinel-policies/aws/restrict-ami-owners.sentinel
cp /tmp/terraform-sentinel-policies/aws/mocks/ec2-instance-mock-tfrun.sentinel \
  src/test/resources/sentinel/terraform-sentinel-policies/aws/mocks/ec2-instance-mock-tfrun.sentinel
cp /tmp/terraform-sentinel-policies/cloud-agnostic/prohibited-providers.sentinel \
  src/test/resources/sentinel/terraform-sentinel-policies/cloud-agnostic/prohibited-providers.sentinel
cp /tmp/terraform-sentinel-policies/common-functions/report/report.sentinel \
  src/test/resources/sentinel/terraform-sentinel-policies/common-functions/report/report.sentinel
```

更新時は、`SentinelParserUnitTest` の互換サブセット/未対応サブセット分類が妥当か必ず見直してください。
