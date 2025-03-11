# Expense Income Project

---

## Qeyd: bu emrler ardicilliqla olmalidir

---

## docker ucun ilk novbede asagidaki emrini yerine yetirmeliyik bu zaman dockerde containerler yaranir bir ad altinda gorunur
```bash
docker-compose up --build
```
## docker huba push etmek ucun

### Proyekti push etmek ucun
```bash
docker tag expense-income-app:latest shahrigul/expense-income-app:app
docker push shahrigul/expense-income-app:app
```

### Database i push etmek ucun
```bash
docker tag oscarfonts/h2:latest shahrigul/expense-income-app:h2
docker push shahrigul/expense-income-app:h2
```

## H2 bazasi url ile run edecik browserde
    - localhost:8082 
